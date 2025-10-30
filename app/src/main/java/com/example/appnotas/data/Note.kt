package com.example.appnotas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val dateCreated: Long = System.currentTimeMillis(),
    val tipo: String,
    val recordatorio: Long? = null,
    val completa: Boolean = false
)
