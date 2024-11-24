package com.example.tv_shows_app_kotlin.utilities;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import kotlin.jvm.JvmStatic;

object BindingAdapters {

@BindingAdapter("android:imageURL")
@JvmStatic
fun setImageURL(imageView: ImageView, URL: String?) {
    try {
        imageView.alpha = 0f
        Picasso.get().load(URL).noFade().into(imageView, object : Callback {
            override fun onSuccess() {
                imageView.animate().setDuration(300).alpha(1f).start()
            }

            override fun onError(e: Exception?) {
                // Handle the error if needed
            }
        })
    } catch (ignored: Exception) {
        // Ignored exception handling
    }
}
}
