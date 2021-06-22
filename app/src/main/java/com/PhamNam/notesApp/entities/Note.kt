package com.PhamNam.notesApp.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")

class Note  : Serializable {
    @PrimaryKey(autoGenerate = true) var id : Int = 0

    @ColumnInfo(name = "title")
     var title : String = ""

    @ColumnInfo(name = "data_time")
    var dataTime : String = ""

    @ColumnInfo(name = "subtitle")
     var subtitle : String = ""

    @ColumnInfo(name = "note_text")
     var noteText : String =""

    @ColumnInfo(name = "image_path")
     var imagePath : String = ""

    @ColumnInfo(name = "color")
    var color : String = ""

    @ColumnInfo(name = "web_link")
     var webLink : String = ""
    @NonNull
    override fun toString(): String {
        return "$title : $dataTime"
    }
}

