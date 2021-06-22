package com.PhamNam.notesApp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.PhamNam.notesApp.dao.NoteDao
import com.PhamNam.notesApp.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    companion object {
        @Volatile
        var noteDatabase: NoteDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NoteDatabase {
            return noteDatabase ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()
                noteDatabase = instance
                instance
            }
        }

        private class NoteDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                noteDatabase?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.noteDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(noteDao: NoteDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            noteDao.deleteAllNote()
        }

    }

}