package com.example.appnotas.data.local

class NoteRepository(
    private val noteDao: NoteDao
) {
    fun getAllNotes() = noteDao.getAllNotes()
    fun getAllTasks() = noteDao.getAllTasks()

    fun getNoteById(id: Int) = noteDao.getNoteById(id)

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

}
