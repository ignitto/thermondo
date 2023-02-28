package com.thermondo.notes

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NotesRepositoryTest {

    private lateinit var notesRepository: NotesRepository
    val bobNote1 = Note(title = "Bob Title1", body = "Bob Body1", tags = "Bob tag1")
    val bobNote2 = Note(title = "Bob Title2", body = "Bob Body2", tags = "Bob tag2")
    val jenNote = Note(title = "Jen Title2", body = "Jen Body2", tags = "Jen tag2")

    @BeforeTest
    fun initData() {
        notesRepository = NotesRepository()
        notesRepository.addNotes("Bob", bobNote1)
        notesRepository.addNotes("Bob", bobNote2)
        notesRepository.addNotes("Jen", jenNote)
    }

    @Test
    fun `add and get notes success`() {
        val bobNotes = notesRepository.getNotes("Bob", null)
        assertEquals(2, bobNotes!!.size)
        assertEquals(listOf(bobNote1, bobNote2), bobNotes)

        val jenNotes = notesRepository.getNotes("Jen", null)
        assertEquals(1, jenNotes!!.size)
        assertEquals(listOf(jenNote), jenNotes)
    }

    @Test
    fun `get notes by tag`() {
        val bobNotes = notesRepository.getNotes("Bob", "tag2")
        assertEquals(1, bobNotes!!.size)
        assertEquals(bobNote2.tags, bobNotes[0].tags)
        assertEquals(bobNote2.body, bobNotes[0].body)
        assertEquals( bobNote2.title, bobNotes[0].title)
    }

    @Test
    fun `get notes by non existing tag`() {
        val bobNotes = notesRepository.getNotes("Bob", "non existing tag")
        assertEquals(0, bobNotes!!.size)
    }

    @Test
    fun `update note to public`() {
        val updateNote = bobNote2.copy(public = true)
        notesRepository.updateNotes("Bob", updateNote);
        val bobNotes = notesRepository.getNotes("Bob", "tag2")
        assertEquals(bobNote2.tags, bobNotes!![0].tags)
        assertEquals(bobNote2.body, bobNotes[0].body)
        assertEquals(bobNote2.title, bobNotes[0].title)
        assertEquals(true, bobNotes[0].public)
    }

    @Test
    fun `get only public notes`(){
        val updateNote = bobNote2.copy(public = true)
        notesRepository.updateNotes("Bob", updateNote);

        val publicNotes = notesRepository.getPublicNotes("Bob")
        assertEquals(bobNote2.tags, publicNotes!![0].tags)
        assertEquals(bobNote2.body, publicNotes[0].body)
        assertEquals(bobNote2.title, publicNotes[0].title)
        assertEquals(true, publicNotes[0].public)
    }

    @Test
    fun `delete note success`(){
        val jenNotes = notesRepository.getNotes("Jen", null)
        notesRepository.deleteNotes("Jen", jenNotes!![0].id)

        val jenNotesAfterDelete = notesRepository.getNotes("Jen", null)
        assertEquals(0, jenNotesAfterDelete!!.size)
    }
}
