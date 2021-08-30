package com.ekkoe.fitty.data

import kotlinx.serialization.Serializable

@Serializable
data class HomeBanner(
    val desc: String?,
    val id: Int,
    val imagePath: String?,
    val isVisible: Int,
    val order: Int,
    val title: String?,
    val type: Int,
    val url: String?
)