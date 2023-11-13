package com.kingelias.noteapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kingelias.noteapp.data.Constants
import com.kingelias.noteapp.data.Note

class NoteViewModel : ViewModel() {

    // LiveData for the list of notes
    private val _noteList = MutableLiveData<List<Note>>()
    val noteList: LiveData<List<Note>> get() = _noteList

    // LiveData for the currently selected note
    private val _selectedNote = MutableLiveData<Note?>()
    val selectedNote: LiveData<Note?> get() = _selectedNote

    // LiveData to notify observers about note updates
    private val _noteUpdated = MutableLiveData<Boolean>()
    val noteUpdated: LiveData<Boolean> get() = _noteUpdated

    // LiveData for reporting errors or results
    private val _result = MutableLiveData<Exception>()
    val result: LiveData<Exception> get() = _result

    // Firebase Database references
    private val database = FirebaseDatabase.getInstance()
    private val dbNotes = database.reference.child(Constants.NODE_NOTE)

    // Set the selected note
    fun setSelectedNote(note: Note) {
        _selectedNote.value = note
    }

    // Clear the selected note
    fun clearSelectedNote() {
        _selectedNote.value = null
    }

    // Fetch notes from Firebase
    fun fetchNotes() {
        dbNotes.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // List to hold retrieved notes
                    val noteList = mutableListOf<Note>()

                    // Iterate through Firebase data snapshot to extract notes
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(Note::class.java)

                        // Set note id from Firebase key
                        note?.id = noteSnapshot.key.toString()

                        if (note != null) {
                            noteList.add(note)
                        }
                    }

                    // Update LiveData with the fetched notes
                    _noteList.value = noteList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors in fetching notes and update result LiveData
                _result.value = Exception(error.message)
            }
        })
    }

    // Create a new note in Firebase
    fun createNote(note: Note) {
        // Generate a unique key for the new note
        note.id = dbNotes.push().key

        dbNotes.child(note.id!!).setValue(note)
            .addOnSuccessListener {
                // Indicate successful note creation
                _noteUpdated.value = true
            }
            .addOnFailureListener {
                // Update result LiveData with the failure exception
                _result.value = it
            }
    }

    // Update a note in Firebase
    fun updateNote(note: Note) {
        dbNotes.child(note.id!!).setValue(note)
            .addOnSuccessListener {
                // Indicate successful note update
                _noteUpdated.value = true
            }
            .addOnFailureListener {
                // Update result LiveData with the failure exception
                _result.value = it
            }
    }

    // Delete a note from Firebase
    fun deleteNote(note: Note) {
        dbNotes.child(note.id!!).setValue(null)
            .addOnSuccessListener {
                // Indicate successful note deletion
                _noteUpdated.value = true
            }
            .addOnFailureListener {
                // Update result LiveData with the failure exception
                _result.value = it
            }
    }

}
