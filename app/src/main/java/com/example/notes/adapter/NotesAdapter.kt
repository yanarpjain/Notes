package com.example.notes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.MainActivity
import com.example.notes.R
import com.example.notes.models.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listner: MainActivity): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {


    private val NoteList= ArrayList<Note>()
    private val fullList= ArrayList<Note>()
    /*private val noteColors: MutableMap<Int, Int> = mutableMapOf()*/
    private val noteColors = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote= NoteList[position]
        holder.title.text= currentNote.title
        holder.title.isSelected= true
        holder.note.text= currentNote.note
        holder.date.text= currentNote.date
        holder.date.isSelected= true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener{

            listner.onItemClickd(NoteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener{
             listner.onLongItemclicked(NoteList[holder.adapterPosition], holder.notes_layout)
             true
        }

    }


    fun updateList(newList: List<Note>)
    {
        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(fullList)


        // Store colors for notes
        for (note in NoteList) {
            if (!noteColors.containsKey(note.id)) {
                noteColors[note.id ?: 0] = randomColor()

            }
        }

        notifyDataSetChanged()
    }


    fun filterList(search: String) {
        NoteList.clear()

        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                NoteList.add(item)
            }
        }

        notifyDataSetChanged()
    }


    private fun randomColor(): Int{
        val list= ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex= Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

inner class NoteViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
                val notes_layout= itemView.findViewById<CardView>(R.id.card_layout)
                val title= itemView.findViewById<TextView>(R.id.tv_title)
                val note= itemView.findViewById<TextView>(R.id.tv_note)
                val date= itemView.findViewById<TextView>(R.id.tv_date)

    }


    interface NotesClickListner{

        fun onItemClickd(note: Note)

        fun onLongItemclicked(note: Note, cardView: CardView)
    }

}