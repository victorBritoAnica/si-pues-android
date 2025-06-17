package com.sipues.di

import android.content.Context
import androidx.room.Room
import com.sipues.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.sipues.data.local.BusinessDao
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "si-pues-db" // Nombre del archivo de la base de datos
        ).build()
    }

    @Provides
    fun provideBusinessDao(database: AppDatabase): BusinessDao {
        return database.businessDao()
    }
}