package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_chatroom(
    val token: String,
    val type: String,
    val name: String,
) : Serializable
