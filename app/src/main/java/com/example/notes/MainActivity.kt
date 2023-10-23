package com.example.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.adapter.NotesAdapter
import com.example.notes.database.NoteDatabase
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Note
import com.example.notes.models.NoteViewModel

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListner, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
     lateinit var viewModel : NoteViewModel
     lateinit var adapter: NotesAdapter
     lateinit var selectedNote: Note


     private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

         if( result.resultCode == Activity.RESULT_OK)
         {
             val note= result.data?.getSerializableExtra("note") as? Note
             if ( note!= null)
             {
                 viewModel.updateNote(note)
             }
         }

     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]
//initialize the UI
        initUi()

    }

    private fun initUi() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager= StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter= NotesAdapter(this,this)
        binding.recyclerView.adapter= adapter

        // Fetch the initial list of notes from the ViewModel
        val initialNotesLiveData = viewModel.allnotes // Use the correct LiveData variable name
        initialNotesLiveData.observe(this) { notes ->
            val initialNotes: List<Note> = notes ?: emptyList()
            adapter.updateList(initialNotes)
        }


        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if(result.resultCode == Activity.RESULT_OK)
            {
                val note= result.data?.getSerializableExtra("note") as? Note
                if ( note!= null)
                {
                    viewModel.insertNote(note)
                }
            }
        }

        binding.fbAddNote.setOnClickListener{

            val intent= Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!= null)
                {
                    adapter.filterList(newText)
                }
                return true
            }

        })

    }

    override fun onItemClickd(note: Note) {
        val intent= Intent(this, AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemclicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
            val popup= PopupMenu(this, cardView)
            popup.setOnMenuItemClickListener(this@MainActivity)
            popup.inflate(R.menu.pop_up_menu)
            popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note)
        {
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}