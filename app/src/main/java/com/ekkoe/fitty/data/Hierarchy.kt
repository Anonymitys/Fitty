package com.ekkoe.fitty.data

import kotlinx.serialization.Serializable

@Serializable
data class Hierarchy(
    val children: ArrayList<SubHierarchy>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
):java.io.Serializable

@Serializable
data class SubHierarchy(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
):java.io.Serializable