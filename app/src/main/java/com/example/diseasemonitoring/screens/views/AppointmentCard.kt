package com.example.diseasemonitoring.screens.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.diseasemonitoring.models.Appointment
import com.example.diseasemonitoring.models.Disease


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseaseMedicationCard(appointment: Appointment,
                          onDelete:(String) -> Unit,
                          onUpdate:(Appointment) ->Unit

) {

    var showDialog = remember{ mutableStateOf(false) }
    var editableAppointment = remember { mutableStateOf( appointment.copy(
        name = appointment.name ?: "",
        description = appointment.description ?: "",
        notificationTime = appointment.notificationTime ?: "",
    )) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {onDelete(appointment.name)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = {showDialog.value=true}) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Update",
                        tint = Color.Blue
                    )
                }
            }


            Text(text = "Appointment: ${appointment.name}", style = MaterialTheme.typography.titleMedium)
            if (appointment.description.isNotEmpty()) {
                Text(text = "Description: ${appointment.description}", style = MaterialTheme.typography.bodyMedium)
            }

            // Notification Time
            if (appointment.notificationTime.isNotEmpty()) {
                Text(text = "Notification Time: ${appointment.notificationTime}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Edit Disease", style = MaterialTheme.typography.titleMedium) },
            text = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Disease Name
                    OutlinedTextField(
                        value = editableAppointment.value.name,
                        onValueChange = { editableAppointment.value = editableAppointment.value.copy(name = it) },
                        label = { Text("Appointment Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // description
                    OutlinedTextField(
                        value = editableAppointment.value.description,
                        onValueChange = { editableAppointment.value = editableAppointment.value.copy(description = it) },
                        label = { Text("description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notification Time
                    OutlinedTextField(
                        value = editableAppointment.value.notificationTime,
                        onValueChange = { editableAppointment.value = editableAppointment.value.copy(notificationTime = it) },
                        label = { Text("Notification Time") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )


                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editableAppointment.value.name.isNotBlank() ) {
                            onUpdate(editableAppointment.value)
                            showDialog.value = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Update", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}