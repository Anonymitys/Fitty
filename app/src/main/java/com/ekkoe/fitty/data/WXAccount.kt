package com.ekkoe.fitty.data

import kotlinx.serialization.Serializable

@Serializable
data class ChapterTab(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)