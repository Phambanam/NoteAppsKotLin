package com.PhamNam.notesApp.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.PhamNam.notesApp.R
import com.PhamNam.notesApp.adapter.NoteListAdapter.*
import com.PhamNam.notesApp.entities.Note
import com.PhamNam.notesApp.listeners.NotesListener

class NoteListAdapter(
    private var notesListener: NotesListener
) : ListAdapter<Note, NoteViewHolder>(NOTE_COMPARATOR){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title,current.subtitle,current.dataTime,current.color,current.imagePath)
        holder.layoutRecyclerView.setOnClickListener{
            notesListener.onNoteClicked(getItem(position),position)

        }

    }

     class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val noteItemView : TextView = itemView.findViewById(R.id.textTitle)
        private val noteSubtitle : TextView = itemView.findViewById(R.id.textSubtitle)
        private val noteDataText : TextView = itemView.findViewById(R.id.textDataTime)
        private val imageNote : ImageView = itemView.findViewById(R.id.imageNotePreview)
        val layoutRecyclerView :LinearLayout = itemView.findViewById(R.id.layoutNote)
        fun bind(textTitle : String?, textSubtitle :String?,textDataTime : String?,textNoteColor :String?,imagePath: String?){
            noteItemView.text = textTitle
            noteSubtitle.text = textSubtitle
            noteDataText.text = textDataTime
            Log.d("aaaaa",imagePath + "nam")
            if (imagePath == "") imageNote.visibility = View.GONE
            else {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                imageNote.visibility = View.VISIBLE

            }
            val gradientDrawable = layoutRecyclerView.background as GradientDrawable
            if (textNoteColor == "") gradientDrawable.setColor(Color.parseColor("#333333"))
            else gradientDrawable.setColor(Color.parseColor(textNoteColor))

        }
        companion object{
            fun create(parent : ViewGroup) : NoteViewHolder{
                val view : View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item,parent,false)
                return NoteViewHolder(view)
            }


        }


     }
    companion object{
        private val NOTE_COMPARATOR = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
               return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}