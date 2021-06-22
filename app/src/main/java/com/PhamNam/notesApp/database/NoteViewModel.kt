package com.PhamNam.notesApp.adapter

import androidx.lifecycle.*
import com.PhamNam.notesApp.entities.Note
import com.PhamNam.notesApp.repositories.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NoteViewModel(private  val repository: NoteRepository) : ViewModel() {

    val allNotes : LiveData<List<Note>> = repository.allNotes.asLiveData()
    fun insertNote(note : Note) = viewModelScope.launch {
        repository.insert(note)
    }
    fun updateNote(note : Note) = viewModelScope.launch {
        repository.update(note)
    }
    fun deleteNote(note : Note) = viewModelScope.launch {
        repository.delete(note)
    }
    fun searchNotes(title : String) : LiveData<List<Note>> =  repository.search(title).asLiveData()
}
class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T

        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}