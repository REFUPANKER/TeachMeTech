package com.refupanker.teachmetech.model

import java.io.Serializable


data class mdl_baninfo(
    val token: String,
    val reason: String,
    val until: String,
    ) : Serializable
