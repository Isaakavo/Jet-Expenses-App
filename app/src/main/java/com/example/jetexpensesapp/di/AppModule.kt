package com.example.jetexpensesapp.di

import android.content.Context
import androidx.room.Room
import com.example.jetexpensesapp.data.UdiDatabase
import com.example.jetexpensesapp.data.UdiDatabaseDao
import com.example.jetexpensesapp.network.UdiApi
import com.example.jetexpensesapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUdisDao(udiDatabase: UdiDatabase): UdiDatabaseDao = udiDatabase.udiDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): UdiDatabase =
        Room.databaseBuilder(context, UdiDatabase::class.java, "udis_db")
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideUdiApi(): UdiApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UdiApi::class.java)
    }
}