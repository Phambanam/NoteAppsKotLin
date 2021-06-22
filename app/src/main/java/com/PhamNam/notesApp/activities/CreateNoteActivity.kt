@file:Suppress("DEPRECATION", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")

package com.PhamNam.notesApp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.PhamNam.notesApp.R
import com.PhamNam.notesApp.adapter.NoteViewModel
import com.PhamNam.notesApp.adapter.NoteViewModelFactory
import com.PhamNam.notesApp.database.NotesApplication
import com.PhamNam.notesApp.databinding.ActivityCreatNoteBinding
import com.PhamNam.notesApp.entities.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws


@Suppress("DEPRECATION")
class CreateNoteActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_IMAGE = 1
        const val REQUEST_CODE_DELETE_NOTE = 2
    }
    private val noteViewModel : NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }
    private lateinit var binding: ActivityCreatNoteBinding
    private lateinit var selectedNoteColor: String
    private lateinit var selectImagePath: String
    private var dialogAddURL: AlertDialog? = null
    private var alreadyAvailableNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatNoteBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.imageBack.setOnClickListener { onBackPressed() }

        binding.textDateTime.text =
            SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(Date())
        selectedNoteColor = "#333333"
        selectImagePath = ""
        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = intent.getSerializableExtra("note") as Note
            setViewOrUpdateNote()
        }
        binding.imageSave.setOnClickListener { saveNote() }
        setSubtitleIndicatorColor()
        initMiscellaneous()
        binding.imageDelete.setOnClickListener {
            deleteNote()

        }

    }

    private fun saveNote() {
        val replyIntent = Intent()
        if (binding.inputNoteTitle.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "note title can't be empty", Toast.LENGTH_SHORT).show()
            return
        } else if (binding.inputNoteSubtitle.text.toString().trim().isEmpty() &&
            binding.inputNote.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(this, "note can't be empty", Toast.LENGTH_SHORT).show()
            return
        } else {
            val note = Note()
            note.title = binding.inputNoteTitle.text.toString()
            note.subtitle = binding.inputNoteSubtitle.text.toString()
            note.noteText = binding.inputNote.text.toString()
            note.dataTime = binding.textDateTime.text.toString()
            note.color = selectedNoteColor
            note.imagePath = selectImagePath

            if (binding.layoutWebURL.visibility == View.VISIBLE) {
                note.webLink = binding.textWebURL.text.toString()
            }
            if (alreadyAvailableNote != null) note.id = alreadyAvailableNote?.id!!
            if(binding.layoutWebURL.visibility == View.VISIBLE)
            note.webLink = binding.textWebURL.text.toString()
            replyIntent.putExtra("note", note)
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()


    }

    private fun initMiscellaneous() {
        val layoutMiscellaneous = findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)
        layoutMiscellaneous.findViewById<TextView>(R.id.textMiscellaneous).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED

            } else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
        val imageViewColor1 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1)
        val imageViewColor2 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2)
        val imageViewColor3 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3)
        val imageViewColor4 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4)
        val imageViewColor5 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor5)

        imageViewColor1.setOnClickListener {
            selectedNoteColor = "#333333"
            imageViewColor1.setImageResource(R.drawable.ic_done)
            imageViewColor2.setImageResource(0)
            imageViewColor3.setImageResource(0)
            imageViewColor4.setImageResource(0)
            imageViewColor5.setImageResource(0)
            setSubtitleIndicatorColor()

        }
        imageViewColor2.setOnClickListener {
            selectedNoteColor = "#FDBE3B"
            imageViewColor1.setImageResource(0)
            imageViewColor2.setImageResource(R.drawable.ic_done)
            imageViewColor3.setImageResource(0)
            imageViewColor4.setImageResource(0)
            imageViewColor5.setImageResource(0)
            setSubtitleIndicatorColor()
        }
        imageViewColor3.setOnClickListener {
            imageViewColor1.setImageResource(0)
            imageViewColor2.setImageResource(0)
            imageViewColor3.setImageResource(R.drawable.ic_done)
            imageViewColor4.setImageResource(0)
            imageViewColor5.setImageResource(0)
            selectedNoteColor = "#FF4842"
            setSubtitleIndicatorColor()
        }
        imageViewColor4.setOnClickListener {
            imageViewColor1.setImageResource(0)
            imageViewColor2.setImageResource(0)
            imageViewColor3.setImageResource(0)
            imageViewColor4.setImageResource(R.drawable.ic_done)
            imageViewColor5.setImageResource(0)
            selectedNoteColor = "#3A52FC"
            setSubtitleIndicatorColor()
        }
        imageViewColor5.setOnClickListener {
            imageViewColor1.setImageResource(0)
            imageViewColor2.setImageResource(0)
            imageViewColor3.setImageResource(0)
            imageViewColor4.setImageResource(0)
            imageViewColor5.setImageResource(R.drawable.ic_done)
            selectedNoteColor = "#000000"
            setSubtitleIndicatorColor()
        }
        if (alreadyAvailableNote != null && alreadyAvailableNote!!.color.trim().isNotEmpty()) {
            when (alreadyAvailableNote!!.color) {
                "#FDBE3B" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor2).performClick()
                "#FF4842" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor3).performClick()
                "#3A52FC" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor4).performClick()
                "#000000" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor5).performClick()
            }

        }

        val layoutAddImage: LinearLayout = layoutMiscellaneous.findViewById(R.id.layoutAddImage)
        layoutAddImage.setOnClickListener {
            requestPermission()
        }
        val layoutAddURl: LinearLayout = layoutMiscellaneous.findViewById(R.id.layoutAddURL)
        layoutAddURl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddUrlDialog()
        }

    }

    private fun setSubtitleIndicatorColor() {
        val gradientDrawable = binding.viewSubtitleIndicator.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor))


    }


    private fun requestPermission() {

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                openImagePicker()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@CreateNoteActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()

    }

    @Throws
    private fun openImagePicker() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_CODE_IMAGE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            binding.imageNote.setImageURI(data?.data)
            binding.imageNote.visibility = View.VISIBLE
            selectImagePath = getPath(data?.data!!)
        }

    }

    @SuppressLint("Recycle")
    private fun getPath(contentUri: Uri): String {
        val cursor: Cursor = contentResolver.query(contentUri, null, null, null, null) as Cursor
        cursor.moveToFirst()
        val index = cursor.getColumnIndex("_data")
        val filePath: String = cursor.getString(index)
        cursor.close()
        return filePath
    }

    private fun showAddUrlDialog() {

        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this)
            .inflate(R.layout.layout_add_url, findViewById(R.id.layoutAddUrlContainer))
        builder.setView(view)
        dialogAddURL = builder.create()
        dialogAddURL!!.window?.setBackgroundDrawable(ColorDrawable(0))
        val inputURL = view.findViewById<EditText>(R.id.inputURL)
        inputURL.requestFocus()
        view.findViewById<TextView>(R.id.textAdd).setOnClickListener {

            if (inputURL.text.toString().trim().isEmpty()) Toast.makeText(
                this,
                "Enter URL",
                Toast.LENGTH_SHORT
            ).show()
            else if (!Patterns.WEB_URL.matcher(inputURL.text.toString()).matches()) {
                Toast.makeText(this, "Enter valid URL", Toast.LENGTH_SHORT).show()
            } else {
                binding.textWebURL.text = inputURL.text
                binding.layoutWebURL.visibility = View.VISIBLE
                dialogAddURL?.dismiss()
            }

        }
        view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
            dialogAddURL?.dismiss()
        }
        dialogAddURL?.show()
    }

    private fun setViewOrUpdateNote() {
        binding.inputNoteTitle.setText(alreadyAvailableNote?.title)
        binding.textDateTime.text = alreadyAvailableNote?.dataTime
        binding.inputNoteSubtitle.setText(alreadyAvailableNote?.subtitle)
        binding.inputNote.setText(alreadyAvailableNote?.noteText)
        selectedNoteColor = alreadyAvailableNote?.color!!
        if (alreadyAvailableNote!!.imagePath.trim()
                .isNotEmpty()
        ) {
            binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote!!.imagePath))
            binding.imageNote.visibility = View.VISIBLE
            selectImagePath = alreadyAvailableNote!!.imagePath
        }
        if (alreadyAvailableNote!!.webLink.trim()
                .isNotEmpty()
        ) {
            binding.textWebURL.text = alreadyAvailableNote!!.webLink
            binding.layoutWebURL.visibility = View.VISIBLE

        }

    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm delete note")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            alreadyAvailableNote?.let { noteViewModel.deleteNote(it) }
            startActivityForResult(
                Intent(applicationContext, MainActivity::class.java),
                REQUEST_CODE_DELETE_NOTE
            )
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }

        builder.show()
    }

}