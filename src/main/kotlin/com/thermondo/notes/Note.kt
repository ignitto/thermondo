package com.thermondo.notes

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Note (
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val tags: String,
    val public: Boolean = false
)
