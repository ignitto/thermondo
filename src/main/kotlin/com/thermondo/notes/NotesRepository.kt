package com.thermondo.notes

import org.slf4j.LoggerFactory

class NotesRepository {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val notesRepo = mutableMapOf<String, MutableList<Note>>()

    fun getNotes(username: String, tag: String?): MutableList<Note>? {
        if (tag != null) {
            return notesRepo[username]
                ?.stream()
                ?.filter { it.tags.contains(tag) }
                ?.toList()
        }
        return notesRepo[username]
    }

    fun getPublicNotes(username: String): MutableList<Note>? {
        return notesRepo[username]
            ?.stream()
            ?.filter { it.public }
            ?.toList()
    }

    fun addNotes(username: String, note: Note) {
        val notes = notesRepo[username]
        if (notes == null) {
            notesRepo[username] = mutableListOf(note)
            logger.info("Note was added")
        } else {
            notes.add(note)
            notesRepo[username] = notes
            logger.info("Note was added")
        }
    }

    fun updateNotes(username: String, note: Note) {
        val notes = notesRepo[username]
        if (notes == null) {
            logger.info("There are no Notes to update")
            return
        }

        notes.forEachIndexed { i, value ->
            run {
                if (value.id == note.id) {
                    notes[i] = note
                }
            }

            logger.info(" Note id: ${note.id} was updated")
        }
    }

    fun deleteNotes(username: String, id: String) {
        val deleted = notesRepo[username]?.removeIf { it.id == id }
        if (deleted == true) {
            logger.info("Note id: $id was deleted")
        } else {
            logger.info("Note id: $id was NOT deleted")
        }
    }
}
