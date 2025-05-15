package com.sipues.data.model.response

data class Business(
    val id: Long,
    val name: String,
    val description: String,
    val address: String,
    val categoryId: Long,
    val imageUrl: String?,
    val latitude: String,
    val longitude: String,
    val createdAt: String,
    val municipioId: Long
)