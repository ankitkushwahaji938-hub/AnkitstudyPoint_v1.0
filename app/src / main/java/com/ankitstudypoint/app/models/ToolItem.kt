package com.ankitstudypoint.app.models

data class ToolItem(
    val id: Int,
    val name: String,
    val description: String,
    val emoji: String,
    val url: String,
    val colorRes: Int,
    val isFeatured: Boolean = false
)
