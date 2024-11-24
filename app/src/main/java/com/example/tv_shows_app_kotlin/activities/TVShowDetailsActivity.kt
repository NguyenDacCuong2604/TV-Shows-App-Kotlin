package com.example.tv_shows_app_kotlin.activities

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.tv_shows_app_kotlin.R
import com.example.tv_shows_app_kotlin.adapters.EpisodesAdapter
import com.example.tv_shows_app_kotlin.adapters.ImageSliderAdapter
import com.example.tv_shows_app_kotlin.databinding.ActivityTvshowDetailsBinding
import com.example.tv_shows_app_kotlin.databinding.LayoutEpisodesBottomSheetBinding
import com.example.tv_shows_app_kotlin.models.TVShow
import com.example.tv_shows_app_kotlin.utilities.TempDataHolder
import com.example.tv_shows_app_kotlin.viewmodels.TVShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class TVShowDetailsActivity : AppCompatActivity() {

    private lateinit var activityTvshowDetailsBinding: ActivityTvshowDetailsBinding
    private lateinit var tvShowDetailsViewModel: TVShowDetailsViewModel
    private var episodesBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var layoutEpisodesBottomSheetBinding: LayoutEpisodesBottomSheetBinding
    private lateinit var tvShow: TVShow
    private var isTVShowAvailableInWatchlist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details)
        doInitialization()
    }

    private fun doInitialization() {
        tvShowDetailsViewModel = ViewModelProvider(this).get(TVShowDetailsViewModel::class.java)
        activityTvshowDetailsBinding.imageBack.setOnClickListener { onBackPressed() }
        tvShow = intent.getSerializableExtra("tvShow") as TVShow
        checkTVShowInWatchlist()
        getTVShowDetails()
    }

    private fun checkTVShowInWatchlist() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            tvShowDetailsViewModel.getTVShowFromWatchlist(tvShow.id.toString())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isTVShowAvailableInWatchlist = true
                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added)
                    compositeDisposable.dispose()
                }
        )
    }

    private fun getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true)
        val tvShowId = tvShow.id.toString()

        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this) { tvShowDetailsResponse ->
            activityTvshowDetailsBinding.setIsLoading(false)
            tvShowDetailsResponse.tvShowDetails?.let { tvShowDetails ->
                tvShowDetails.pictures?.let { loadImageSlider(it) }
                activityTvshowDetailsBinding.setTvShowImageURL(tvShowDetails.imagePath)
                activityTvshowDetailsBinding.imageTVShow.visibility = View.VISIBLE

                activityTvshowDetailsBinding.setDescription(
                    HtmlCompat.fromHtml(tvShowDetails.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                )
                activityTvshowDetailsBinding.textDescription.visibility = View.VISIBLE
                activityTvshowDetailsBinding.textReadMore.visibility = View.VISIBLE
                activityTvshowDetailsBinding.textReadMore.setOnClickListener {
                    if (activityTvshowDetailsBinding.textReadMore.text.toString() == "Read More") {
                        activityTvshowDetailsBinding.textDescription.maxLines = Int.MAX_VALUE
                        activityTvshowDetailsBinding.textDescription.ellipsize = null
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less)
                    } else {
                        activityTvshowDetailsBinding.textDescription.maxLines = 4
                        activityTvshowDetailsBinding.textDescription.ellipsize = TextUtils.TruncateAt.END
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more)
                    }
                }

                activityTvshowDetailsBinding.setRating(String.format(Locale.getDefault(), "%.2f", tvShowDetails.rating.toDouble()))
                activityTvshowDetailsBinding.setGenre(tvShowDetails.genres?.get(0) ?: "N/A")
                activityTvshowDetailsBinding.setRuntime("${tvShowDetails.runtime} Min")
                activityTvshowDetailsBinding.viewDivider1.visibility = View.VISIBLE
                activityTvshowDetailsBinding.layoutMisc.visibility = View.VISIBLE
                activityTvshowDetailsBinding.viewDivider2.visibility = View.VISIBLE
                activityTvshowDetailsBinding.buttonWebsite.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tvShowDetails.url))
                    startActivity(intent)
                }
                activityTvshowDetailsBinding.buttonWebsite.visibility = View.VISIBLE
                activityTvshowDetailsBinding.buttonEpisodes.visibility = View.VISIBLE
                activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener {
                    if (episodesBottomSheetDialog == null) {
                        episodesBottomSheetDialog = BottomSheetDialog(this)
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(this), R.layout.layout_episodes_bottom_sheet, findViewById(R.id.episodesContainer), false
                        )
                        episodesBottomSheetDialog?.setContentView(layoutEpisodesBottomSheetBinding.root)
                        layoutEpisodesBottomSheetBinding.episodesRecyclerView.adapter =
                            EpisodesAdapter(tvShowDetails.episodes)
                        layoutEpisodesBottomSheetBinding.textTitle.text =
                            String.format("Episodes | %s", tvShow.name)
                        layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener { episodesBottomSheetDialog?.dismiss() }
                    }

                    val frameLayout: FrameLayout? = episodesBottomSheetDialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    frameLayout?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(frameLayout)
                        bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    episodesBottomSheetDialog?.show()
                }

                activityTvshowDetailsBinding.imageWatchlist.setOnClickListener {
                    val compositeDisposable = CompositeDisposable()
                    if (isTVShowAvailableInWatchlist) {
                        compositeDisposable.add(
                            tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    isTVShowAvailableInWatchlist = false
                                    TempDataHolder.isWatchlistUpdated = true
                                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist)
                                    Toast.makeText(applicationContext, "Removed from watchlist", Toast.LENGTH_SHORT).show()
                                    compositeDisposable.dispose()
                                }
                        )
                    } else {
                        compositeDisposable.add(
                            tvShowDetailsViewModel.addToWatchlist(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    TempDataHolder.isWatchlistUpdated = true
                                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added)
                                    Toast.makeText(applicationContext, "Added to watchlist", Toast.LENGTH_SHORT).show()
                                    compositeDisposable.dispose()
                                }
                        )
                    }
                }
                activityTvshowDetailsBinding.imageWatchlist.visibility = View.VISIBLE
                loadBasicTVShowDetails()
            }
        }
    }

    private fun loadImageSlider(sliderImages: Array<String>) {
        activityTvshowDetailsBinding.sliderViewPager.offscreenPageLimit = 1
        activityTvshowDetailsBinding.sliderViewPager.adapter = ImageSliderAdapter(sliderImages)
        activityTvshowDetailsBinding.sliderViewPager.visibility = View.VISIBLE
        activityTvshowDetailsBinding.viewFadingEdge.visibility = View.VISIBLE
        setupSliderIndicators(sliderImages.size)
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentSliderIndicator(position)
            }
        })
    }

    private fun setupSliderIndicators(count: Int) {
        val indicators = Array(count) { ImageView(applicationContext) }
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        indicators.forEachIndexed { i, imageView ->
            imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.background_sider_indicator_inactive))
            imageView.layoutParams = layoutParams
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(imageView)
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.visibility = View.VISIBLE
        setCurrentSliderIndicator(0)
    }

    private fun setCurrentSliderIndicator(position: Int) {
        val childCount = activityTvshowDetailsBinding.layoutSliderIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.background_slider_indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.background_sider_indicator_inactive))
            }
        }
    }

    private fun loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(tvShow.name)
        activityTvshowDetailsBinding.setNetworkCountry("${tvShow.network} (${tvShow.country})")
        activityTvshowDetailsBinding.setStatus(tvShow.status)
        activityTvshowDetailsBinding.setStartedDate(tvShow.startDate)

        activityTvshowDetailsBinding.textName.visibility = View.VISIBLE
        activityTvshowDetailsBinding.textNetworkCountry.visibility = View.VISIBLE
        activityTvshowDetailsBinding.textStarted.visibility = View.VISIBLE
        activityTvshowDetailsBinding.textStatus.visibility = View.VISIBLE
    }
}