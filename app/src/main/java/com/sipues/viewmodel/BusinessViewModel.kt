package com.sipues.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sipues.data.network.BusinessApiService
import com.sipues.data.model.response.Business
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val apiService: BusinessApiService
) : ViewModel() {

    // Estados posibles
    sealed interface BusinessState {
        object Loading : BusinessState
        data class Success(val businessList: List<Business>) : BusinessState
        data class Error(val message: String) : BusinessState
    }

    // Estado actual
    private val _state = MutableStateFlow<BusinessState>(BusinessState.Loading)
    val state: StateFlow<BusinessState> = _state.asStateFlow()

    // Cargar datos del negocio
    fun loadBusiness() {
        viewModelScope.launch {

            try {
                val business = apiService.getBusinessList()
                _state.value = BusinessState.Success(business)
            } catch (e: Exception) {
                _state.value = BusinessState.Error(
                    message = e.message ?: "Error desconocido al cargar el negocio"
                )
            }
        }
    }
}