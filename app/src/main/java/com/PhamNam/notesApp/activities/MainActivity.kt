package com.PhamNam.notesApp.activities

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.PhamNam.notesApp.adapter.NoteListAdapter
import com.PhamNam.notesApp.adapter.NoteViewModel
import com.PhamNam.notesApp.adapter.NoteViewModelFactory
import com.PhamNam.notesApp.database.NotesApplication
import com.PhamNam.notesApp.databinding.ActivityMainBinding
import com.PhamNam.notesApp.entities.Note
import com.PhamNam.notesApp.listeners.NotesListener


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NotesListener {

    private var noteClickedPosition = -1
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    companion object {
        const val REQUEST_CODE_ADD_NOTE = 1
        const val REQUEST_CODE_UPDATE_NOTE = 2
    }

    private lateinit var note: Note
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageAddNoteMain.setOnClickListener {

            startActivityForResult(
                Intent(applicationContext, CreateNoteActivity::class.java),
                REQUEST_CODE_ADD_NOTE
            )
        }
        val adapter = NoteListAdapter(this)
        binding.notesRecyclerView.adapter = adapter
        binding.notesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteViewModel.allNotes.observe(this) { notes ->
            notes.let { adapter.submitList(it) }
        }
        binding.imageSearch.setOnClickListener {
            val strKerWord = binding.inputSearch.text.toString().trim()
            noteViewModel.searchNotes(strKerWord).observe(this) { notes ->
                notes.let { adapter.submitList(it) }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == Activity.RESULT_OK) {
            note = data?.extras?.get("note") as Note
            noteViewModel.insertNote(note)
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == Activity.RESULT_OK) {
            note = data?.extras?.get("note") as Note
            noteViewModel.updateNote(note)
        }
    }

    override fun onNoteClicked(note: Note, position: Int) {
        noteClickedPosition = position
        val intent = Intent(applicationContext, CreateNoteActivity::class.java)
        intent.putExtra("isViewOrUpdate", true)
        intent.putExtra("note", note)
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE)
    }

}
