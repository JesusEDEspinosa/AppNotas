package com.example.appnotas.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.appnotas.data.local.AppDatabase
import com.example.appnotas.data.local.Note
import com.example.appnotas.data.local.NoteRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "notas_db"
    )
        .fallbackToDestructiveMigration()
        .build()
    private val repository = NoteRepository(database.noteDao())

    val allNotes: Flow<List<Note>> = repository.getAllNotes()
    val allTasks: Flow<List<Note>> = repository.getAllTasks()

    fun insertNote(title: String, content: String, tipo: String, recordatorio: Long? = null) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val newNote = Note(
                title = title,
                content = content,
                dateCreated = now,
                tipo = tipo,
                recordatorio = recordatorio
            )
            repository.insert(newNote)
        }
    }
    fun insertTarea(title: String, content: String, tipo: String, recordatorio: Long? = null) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val newNote = Note(
                title = title,
                content = content,
                dateCreated = now,
                tipo = tipo,
                recordatorio = recordatorio
            )
            repository.insert(newNote)
        }
    }
}

