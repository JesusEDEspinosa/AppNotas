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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    val filteredNotes: StateFlow<List<Note>> =
        searchQuery.combine(allNotes) { query, notes ->
            if (query.isBlank()) {
                notes
            } else {
                notes.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.content.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Inicia después de 5s de inactividad
            initialValue = emptyList()
        )

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

    fun getNoteById(id: Int): Flow<Note?> {
        return repository.getNoteById(id)
    }
    fun updateNote(
        id: Int,
        newTitle: String,
        newContent: String,
        newRecordatorio: Long?,
        oldNote: Note
    ) {
        viewModelScope.launch {
            val updatedNote = oldNote.copy(
                id = id,
                title = newTitle,
                content = newContent,
                recordatorio = newRecordatorio
            )
            repository.update(updatedNote)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun taskCompletion(note: Note) {
        if (note.tipo == "Tarea") {
            viewModelScope.launch {
                val updatedNote = note.copy(completa = !note.completa)
                repository.update(updatedNote)
            }
        }
    }

}

