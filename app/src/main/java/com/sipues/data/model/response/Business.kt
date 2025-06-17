package com.sipues.data.model.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "business")
data class Business(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("created_at")
    @ColumnInfo(name = "category_id")
    val createdAt: String,

    @SerializedName("municipio_id")
    @ColumnInfo(name = "municipio_id")
    val municipalityId: Int
)