package com.example.diseasemonitoring.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diseasemonitoring.models.Appointment
import com.example.diseasemonitoring.models.Disease
import com.example.diseasemonitoring.screens.views.DiseaseMedicationCard
import com.example.diseasemonitoring.viewmodels.DiseaseViewModels

@Composable
fun DoctorAppointmentsScreen(
    appointmentList: List<Appointment>,
    viewModel: DiseaseViewModels,
    showAddAppointmentDialog: Boolean,
    onShowAddAppointmentDialogChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Appointment",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        appointmentList.forEach { appointment ->
            DiseaseMedicationCard(
                appointment = appointment,
                onDelete = { appointmentName ->
                    viewModel.deleteAppointment(appointmentName)
                },
                onUpdate = { updateAppointment ->
                    viewModel.updateAppointment(appointmentName = appointment.name, updateAppointment)
                }
            )
        }
    }
}