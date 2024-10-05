package com.refupanker.teachmetech.model

import com.google.firebase.database.ServerValue
import java.io.Serializable

data class mdl_chat_msg(
    val token: String,
    val user: String,
    val sender: String,
    val message: String,
    val timestamp: Map<String, String> = ServerValue.TIMESTAMP
) : Serializable
