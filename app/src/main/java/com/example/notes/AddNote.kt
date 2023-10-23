package com.example.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.ActivityAddNoteBinding
import com.example.notes.models.Note
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldnote: Note
    var isUpdate= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)



        try{
            oldnote= intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(oldnote.title)
            binding.etNote.setText(oldnote.note)
            isUpdate= true

        }catch (e: Exception)
        {
            e.printStackTrace()
        }


        binding.imgChecklist.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val note_desc= binding.etNote.text.toString()

            if(title.isNotEmpty() || note_desc.isNotEmpty())
            {
                val formater = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                note = if( isUpdate) {
                    Note(oldnote.id,title,note_desc,formater.format(Date()))
                } else {
                    Note( null, title, note_desc,formater.format(Date()) )
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else{
                Toast.makeText(this,"please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        binding.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


    }
}