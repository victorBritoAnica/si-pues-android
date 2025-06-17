package com.sipues.data.repository

import com.sipues.data.local.BusinessDao
import com.sipues.data.model.response.Business
import com.sipues.data.network.BusinessApiService
import javax.inject.Inject

class BusinessRepository  @Inject constructor(
    private val apiService: BusinessApiService,
    private val businessDao: BusinessDao
) {
    suspend fun syncBusinesses() {
        val businesses = apiService.getBusiness()
        businessDao.insertBusiness(businesses)
    }

    suspend fun getAllBusiness() : List<Business> {
        return businessDao.getAllBusiness()
    }

    suspend fun getFilteredBusiness(
        municipalityId: Int,
        categoryId: Int
    ): List<Business> {
        return businessDao.getBusinessesByFilters(municipalityId, categoryId)
    }

}