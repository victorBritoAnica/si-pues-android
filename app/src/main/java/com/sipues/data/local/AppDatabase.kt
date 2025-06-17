package com.sipues.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sipues.data.model.response.Business

@Database(
    entities = [Business::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao
}