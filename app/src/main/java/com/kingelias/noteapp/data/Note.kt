package com.kingelias.noteapp.data

import com.google.firebase.database.Exclude

// Data class representing a Note entity
data class Note(
    @get:Exclude
    var id: String? = "", // Unique identifier for the note
    var title: String? = "",
    var content: String? = ""
) {
    // Override equals method for proper comparison
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false

        return true
    }

    // Override hashCode method for proper comparison
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }
}
