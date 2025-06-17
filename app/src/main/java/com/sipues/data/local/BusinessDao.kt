package com.sipues.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sipues.data.model.response.Business

@Dao
interface BusinessDao {

    @Query("SELECT * FROM business WHERE municipio_id = :municipalityId AND category_id = :categoryId")
    suspend fun getBusinessesByFilters(
        municipalityId: Int,
        categoryId: Int
    ): List<Business>


    @Query("SELECT * FROM business")
    suspend fun getAllBusiness(): List<Business>

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // Si hay conflicto (ej: ID repetido), reemplaza el registro
    suspend fun insertBusiness(businesses: List<Business>)
}