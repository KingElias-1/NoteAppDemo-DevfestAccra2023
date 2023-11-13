package com.kingelias.noteapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingelias.noteapp.adapter.NoteAdapter
import com.kingelias.noteapp.data.Note
import com.kingelias.noteapp.databinding.FragmentHomeBinding
import com.kingelias.noteapp.viewmodels.NoteViewModel

class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private val noteViewModel: NoteViewModel by activityViewModels()
    private val noteAdapter = NoteAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up RecyclerView with LinearLayoutManager and adapter
        homeBinding.noteRV.layoutManager = LinearLayoutManager(requireActivity())
        homeBinding.noteRV.adapter = noteAdapter

        // call Fetch notes from ViewModel and observe changes
        noteViewModel.fetchNotes()
        noteViewModel.noteList.observe(viewLifecycleOwner) { notes ->
            Log.i("fire", notes.toString())
            noteAdapter.setNotes(notes)
        }

        // Handle click on FloatingActionButton to add a new note
        homeBinding.newBN.setOnClickListener {
            // Clear the selected note and navigate to the EditFragment
            noteViewModel.clearSelectedNote()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditFragment())
        }

        // Observe noteUpdated LiveData for success message
        noteViewModel.noteUpdated.observe(viewLifecycleOwner) {
            if (it != null && it) {
                Toast.makeText(requireContext(), "Note updated Successfully", Toast.LENGTH_LONG).show()
            }
        }

        // Observe result LiveData for error message
        noteViewModel.result.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }

        return homeBinding.root
    }

    // Function to open a selected note
    fun openNote(note: Note) {
        // Set the selected note and navigate to the EditFragment
        noteViewModel.setSelectedNote(note)
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEditFragment())
    }

    // Function to delete a note
    fun deleteNote(noteItem: Note) {
        noteViewModel.deleteNote(noteItem)
        noteViewModel.fetchNotes()
    }
}
