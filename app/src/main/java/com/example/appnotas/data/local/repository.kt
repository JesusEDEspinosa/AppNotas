package com.example.appnotas.data.local

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getAllNotes()
    fun getAllTasks() = noteDao.getAllTasks()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
}
