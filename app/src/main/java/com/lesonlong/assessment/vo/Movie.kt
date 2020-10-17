package com.lesonlong.assessment.vo

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    val image: List<String>
)