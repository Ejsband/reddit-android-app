package com.project.reddit.di

import android.app.Application
import androidx.room.Room
import com.project.reddit.data.AccessTokenDataDao
import com.project.reddit.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "reddit_data")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAccessTokenDataDao(db: AppDatabase): AccessTokenDataDao {
        return db.accessTokenDataDao()
    }
}