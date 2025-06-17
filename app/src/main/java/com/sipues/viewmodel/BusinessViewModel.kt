package com.sipues.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sipues.data.model.response.Business
import com.sipues.data.repository.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val repository: BusinessRepository
) : ViewModel() {

    // Estados generales para operaciones con negocios
    sealed interface BusinessState {
        object Idle : BusinessState
        object Loading : BusinessState
        data class Success(val businessList: List<Business>) : BusinessState
        data class Error(val message: String) : BusinessState
    }

    // Estados específicos para sincronización
    sealed interface SyncState {
        object Idle : SyncState
        object Loading : SyncState
        object Success : SyncState
        data class Error(val message: String) : SyncState
    }

    // Estados actuales
    private val _businessState = MutableStateFlow<BusinessState>(BusinessState.Idle)
    val businessState: StateFlow<BusinessState> = _businessState.asStateFlow()

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    // Función para sincronizar negocios (optimizada para SplashScreen)
    fun syncBusiness() {
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            try {
                repository.syncBusinesses()
                _syncState.value = SyncState.Success
            } catch (e: Exception) {
                _syncState.value = SyncState.Error("Error al sincronizar: ${e.message}")
            }
        }
    }

    // Cargar todos los negocios
    fun loadBusiness() {
        viewModelScope.launch {
            _businessState.value = BusinessState.Loading
            try {
                val businesses = repository.getAllBusiness()
                _businessState.value = BusinessState.Success(businesses)
            } catch (e: Exception) {
                _businessState.value = BusinessState.Error("Error al cargar negocios: ${e.message}")
            }
        }
    }

    // Cargar negocios filtrados
    fun loadFilteredBusinesses(municipalityId: Int, categoryId: Int) {
        viewModelScope.launch {
            _businessState.value = BusinessState.Loading
            try {
                val businesses = repository.getFilteredBusiness(municipalityId, categoryId)
                _businessState.value = BusinessState.Success(businesses)
            } catch (e: Exception) {
                _businessState.value = BusinessState.Error("Error al filtrar negocios: ${e.message}")
            }
        }
    }

    // Resetear estados
    fun resetBusinessState() {
        _businessState.value = BusinessState.Idle
    }

    fun resetSyncState() {
        _syncState.value = SyncState.Idle
    }
}