package com.PhamNam.notesApp.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.PhamNam.notesApp.dao.NoteDao
import com.PhamNam.notesApp.entities.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val  noteDao: NoteDao) {
    val allNotes : Flow<List<Note>> = noteDao.getAllNotes()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note : Note){
        noteDao.insertNote(note)
    }
    @WorkerThread
    suspend fun update(note: Note){
        noteDao.updateNote(note)
    }
    @WorkerThread
    suspend fun delete(note: Note){
        noteDao.deleteNote(note)
    }

    @WorkerThread
    fun search(title : String) : Flow<List<Note>> {
        return  noteDao.searchNotes(title)
    }


}