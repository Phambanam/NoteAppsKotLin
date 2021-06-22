package com.PhamNam.notesApp.dao

import androidx.room.*
import com.PhamNam.notesApp.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes() : Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNote()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note : Note)
    @Delete
    suspend fun deleteNote(note : Note)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :name || '%' ORDER BY id DESC")
     fun searchNotes(name : String) :  Flow<List<Note>>
}