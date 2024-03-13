package com.cuadrondev.zapetefantasy.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cuadrondev.zapetefantasy.R
import com.cuadrondev.zapetefantasy.model.database.ZapeteFantasyDatabase
import com.cuadrondev.zapetefantasy.model.entities.Player
import com.cuadrondev.zapetefantasy.model.entities.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //DATABASE
    @Singleton
    @Provides
    fun providesZapeteFantasyDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, ZapeteFantasyDatabase::class.java, "ZapeteFantasyDB")
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    var inputStream: InputStream = app.resources.openRawResource(R.raw.players)
                    var sqlQuery = BufferedReader(InputStreamReader(inputStream)).readText()

                    db.execSQL(sqlQuery)

                    inputStream = app.resources.openRawResource(R.raw.users)
                    sqlQuery = BufferedReader(InputStreamReader(inputStream)).readText()

                    db.execSQL(sqlQuery)
                }
            })
            .build()

    //DAOS
    @Singleton
    @Provides
    fun provideUserDao(db: ZapeteFantasyDatabase) = db.userDao()

    @Singleton
    @Provides
    fun providePlayerDao(db: ZapeteFantasyDatabase) = db.playerDao()
}