package com.cuadrondev.zapetefantasy.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.database.ZapeteFantasyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //DATABASE
    @Singleton
    @Provides
    fun providesZapeteFantasyDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, ZapeteFantasyDatabase::class.java, "ZapeteFantasyDB")
            .createFromAsset("database/ZapeteFantasyDB.db")
            .fallbackToDestructiveMigration()
            .build()

    //DAOS
    @Singleton
    @Provides
    fun provideUserDao(db: ZapeteFantasyDatabase) = db.userDao()

    @Singleton
    @Provides
    fun providePlayerDao(db: ZapeteFantasyDatabase) = db.playerDao()
}