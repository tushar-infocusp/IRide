package com.example.iride.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.iride.data.AppContextHolder

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = AppContextHolder.context
    val dbFile = appContext.getDatabasePath("iride.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
