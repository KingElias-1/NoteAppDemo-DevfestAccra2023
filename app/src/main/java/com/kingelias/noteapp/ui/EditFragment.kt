package com.kingelias.noteapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kingelias.noteapp.R
import com.kingelias.noteapp.data.Note
import com.kingelias.noteapp.databinding.FragmentEditBinding
import com.kingelias.noteapp.viewmodels.NoteViewModel

class EditFragment : Fragment() {
    private lateinit var editBinding: FragmentEditBinding
    private val noteViewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        editBinding = FragmentEditBinding.inflate(inflater, container, false)

        // Check if a note is selected (for editing)
        val selectedNote = noteViewModel.selectedNote.value
        if (selectedNote != null) {
            // Populate UI with the selected note's data for editing
            editBinding.titleET.setText(selectedNote.title)
            editBinding.contentET.setText(selectedNote.content)
            editBinding.saveBN.text = getString(R.string.save_note)
        }

        // Handle click on the save button
        editBinding.saveBN.setOnClickListener {
            // Get title and content from the UI
            val title = editBinding.titleET.text.toString().trim()
            val content = editBinding.contentET.text.toString().trim()

            // Check if a note is selected (for editing)
            if (selectedNote != null) {
                // Update the selected note and navigate back
                val updatedNote = Note(selectedNote.id, title, content)
                noteViewModel.updateNote(updatedNote)
                findNavController().navigateUp()
            } else {
                // Create a new note and navigate back
                val newNote = Note(title = title, content = content)
                noteViewModel.createNote(newNote)
                findNavController().navigateUp()
            }
        }

        // Observe noteUpdated LiveData for success message
        noteViewModel.noteUpdated.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Note updated Successfully", Toast.LENGTH_LONG).show()
            }
        }

        // Observe result LiveData for error message
        noteViewModel.result.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        return editBinding.root
    }
}
