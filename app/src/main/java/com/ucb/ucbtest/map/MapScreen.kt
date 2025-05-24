package com.ucb.ucbtest.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val defaultCenter = state.selectedLatLng ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCenter, 5f)
    }
    LaunchedEffect(state.selectedLatLng) {
        state.selectedLatLng?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 12f))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selecciona tu ubicación",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B6B)
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Marca el lugar donde quieres recibir\ntu nueva tarjeta SIM.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                lineHeight = 20.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large),
                cameraPositionState = cameraPositionState,
                onMapClick = { viewModel.onMapClick(it) }
            ) {
                state.selectedLatLng?.let { latLng ->
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Tu ubicación"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Lat: ${String.format("%.4f", state.selectedLatLng?.latitude ?: 0.0)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Lng: ${String.format("%.4f", state.selectedLatLng?.longitude ?: 0.0)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = state.phone,
            onValueChange = viewModel::onPhoneChange,
            label = {
                Text(
                    "Tu número de teléfono",
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF6B6B),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFF6B6B),
                cursorColor = Color(0xFFFF6B6B)
            ),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = viewModel::onSend,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
            shape = MaterialTheme.shapes.large,
            enabled = state.selectedLatLng != null && state.phone.isNotBlank()
        ) {
            Text(
                text = "Enviar tarjeta",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.showConfirmation) {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = {
                    Text(
                        "¡Listo!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                    )
                },
                text = {
                    Text(
                        "Tu tarjeta está en camino a la ubicación seleccionada!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = viewModel::onDismissDialog,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFFF6B6B)
                        )
                    ) {
                        Text(
                            "OK",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                containerColor = Color.White,
                shape = MaterialTheme.shapes.large
            )
        }
    }
}