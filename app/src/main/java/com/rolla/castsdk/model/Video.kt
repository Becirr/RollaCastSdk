package com.rolla.castsdk.model

data class Video(
    val name: String,
    val url: String,
    val streamType: Int,
    val mimeType: String,
    var isSelected: Boolean = false
)