package com.refupanker.teachmetech.model

import java.io.Serializable
import java.util.Date

data class mdl_chat_msg(
    val token: String,
    val sender: String,
    val message: String,
    val date: Date = Date(),
) : Serializable
