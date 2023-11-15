package com.kingelias.noteapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingelias.noteapp.R
import com.kingelias.noteapp.data.Note
import com.kingelias.noteapp.ui.HomeFragment

// Adapter for displaying a list of notes in a RecyclerView
class NoteAdapter(private val fragment: HomeFragment) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var noteList = mutableListOf<Note>()

    // Create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteItem = noteList[position]

        // Set title and preview text
        holder.title.text = noteItem.title
        if (noteItem.content!!.lastIndex < 45) {
            holder.preview.text = noteItem.content?.substring(0, noteItem.content!!.lastIndex + 1) ?: "No preview available"
        } else {
            holder.preview.text = noteItem.content?.substring(0, 45) ?: "No preview available"
        }

        // Set click listeners for delete and item click
        holder.delete.setOnClickListener {
            fragment.deleteNote(noteItem)
        }
        holder.itemView.setOnClickListener {
            fragment.openNote(noteItem)
        }
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return noteList.size
    }

    // ViewHolder class for holding item views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTV)
        val preview: TextView = itemView.findViewById(R.id.previewTV)
        val delete: ImageButton = itemView.findViewById(R.id.deleteBN)
    }

    // Update the list of notes and refresh the UI
    fun setNotes(notes: List<Note>) {
        this.noteList = notes as MutableList<Note>
        notifyDataSetChanged()
    }
}
