package com.refupanker.teachmetech.model

import java.io.Serializable

data class mdl_userprops(
    val token: String,
    val aboutMe: String = "Hello im learning tech with teachmetech",
    val likedCourses: List<String> = listOf(),
    val signedCourses: List<String> = listOf(),
    val badges: List<String> = listOf(),
    val shownBadge: String = "",
) : Serializable
