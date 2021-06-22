package com.PhamNam.notesApp.listeners

import com.PhamNam.notesApp.entities.Note

interface NotesListener {
     fun onNoteClicked(note : Note, position : Int)
}