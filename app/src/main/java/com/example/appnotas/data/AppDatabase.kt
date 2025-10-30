package com.example.appnotas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
