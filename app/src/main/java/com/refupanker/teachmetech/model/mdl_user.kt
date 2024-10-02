package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_user(
    val token: String,
    val name: String,
    val rank: Int = 0,
    val active: Boolean = true,

    ) : Serializable
