package com.example.appnotas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: Note)

    @Query("SELECT * FROM notes ORDER BY dateCreated DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE tipo = 'tarea' ORDER BY recordatorio ASC")
    fun getAllTasks(): Flow<List<Note>>
}
