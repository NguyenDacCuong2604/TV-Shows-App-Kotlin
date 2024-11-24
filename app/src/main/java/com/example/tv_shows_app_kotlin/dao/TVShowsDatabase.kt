package com.example.tv_shows_app_kotlin.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tv_shows_app_kotlin.models.TVShow

@Database(entities = [TVShow::class], version = 1, exportSchema = false)
abstract class TVShowsDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var tvShowsDatabase: TVShowsDatabase? = null

        fun getTvShowsDatabase(context: Context): TVShowsDatabase {
            return tvShowsDatabase ?: synchronized(this) {
                tvShowsDatabase ?: Room.databaseBuilder(
                    context.applicationContext,
                    TVShowsDatabase::class.java,
                    "tv_shows_db"
                ).build().also { tvShowsDatabase = it }
            }
        }
    }

    abstract fun tvShowDao(): TVShowDao
}