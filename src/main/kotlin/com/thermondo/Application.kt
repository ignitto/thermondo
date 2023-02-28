package com.thermondo

import com.thermondo.notes.Note
import com.thermondo.notes.NotesRepository
import com.thermondo.security.User
import com.thermondo.security.UsersRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// NOTE: Referenced in application.conf
@Suppress("unused")
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        json()
    }

    val notesRepository = NotesRepository()
    val usersRepository = UsersRepository()

    install(Authentication) {
        basic("notesAuth") {
            realm = "Access to the '/notes' path"
            validate { credentials ->
                if (usersRepository.isValidUser(credentials.name, credentials.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    routing {

        // Creating new users
        post("/api/users") {
            val user = call.receive<User>()
            try {
                usersRepository.createUser(user.username, user.password)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Forbidden, e.message!!)
            }
            call.respond(HttpStatusCode.Created, "User ${user.username} has been created")
        }

        //Get Public notes
        get("/api/public/{username}/notes") {
            val username: String? = call.parameters["username"] //TODO: validate username
            val notes = notesRepository.getPublicNotes(username!!)
            if (notes == null) {
                call.respond(HttpStatusCode.NotFound, "Notes for user $username was not found!")
            } else {
                call.respond(notes)
            }
        }

        //TODO: test REST endpoints
        authenticate("notesAuth") {

            get("/api/notes") {
                val tag = call.request.queryParameters["tag"]
                val username = call.principal<UserIdPrincipal>()?.name
                val notes = notesRepository.getNotes(username!!, tag)
                if (notes == null) {
                    call.respond(HttpStatusCode.NotFound, "Notes for user $username was not found!")
                } else {
                    call.respond(notes)
                }
            }

            post("/api/notes") {
                val note = call.receive<Note>()
                val username = call.principal<UserIdPrincipal>()?.name
                notesRepository.addNotes(username!!, note)
                call.respond(HttpStatusCode.Created)
            }

            put("/api/notes") {
                val note = call.receive<Note>()
                val username = call.principal<UserIdPrincipal>()?.name
                notesRepository.updateNotes(username!!, note)
                call.respond(HttpStatusCode.OK)
            }

            delete("api/notes/{id}") {
                val username = call.principal<UserIdPrincipal>()?.name
                val id: String? = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    notesRepository.deleteNotes(username!!, id)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }

    }
}
