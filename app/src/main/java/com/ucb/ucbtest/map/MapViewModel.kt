package com.ucb.ucbtest.map

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val selectedLatLng: LatLng? = null,
    val phone: String = "",
    val showConfirmation: Boolean = false
)

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
         //-17.38147115809553, -66.15826112486202
        val center = LatLng(-17.3814, -66.15826)
        _uiState.update { it.copy(selectedLatLng = center) }

        viewModelScope.launch {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            val userLatLng = LatLng(it.latitude, it.longitude)
                            _uiState.update { state -> state.copy(selectedLatLng = userLatLng) }
                        }
                    }
            } else {

            }
        }
    }

    fun onMapClick(latLng: LatLng) {
        _uiState.update { it.copy(selectedLatLng = latLng) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    fun onSend() {
        _uiState.update { it.copy(showConfirmation = true) }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(showConfirmation = false) }
    }
}