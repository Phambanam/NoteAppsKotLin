package com.PhamNam.notesApp.database

import android.app.Application
import com.PhamNam.notesApp.database.NoteDatabase
import com.PhamNam.notesApp.repositories.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { NoteDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}