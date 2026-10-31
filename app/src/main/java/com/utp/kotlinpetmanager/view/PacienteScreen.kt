package com.utp.kotlinpetmanager.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utp.kotlinpetmanager.model.Paciente
import com.utp.kotlinpetmanager.viewmodel.PacienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteScreen(viewModel: PacienteViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KotlinPet Manager 🐾") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ---------- FORMULARIO ----------
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.cambiarNombre(it) },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.especie,
                onValueChange = { viewModel.cambiarEspecie(it) },
                label = { Text("Especie / Raza") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.edad,
                onValueChange = { viewModel.cambiarEdad(it) },
                label = { Text("Edad (años)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.contacto,
                onValueChange = { viewModel.cambiarContacto(it) },
                label = { Text("Contacto de emergencia") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Es urgencia?")
                Switch(
                    checked = viewModel.esUrgencia,
                    onCheckedChange = { viewModel.cambiarUrgencia(it) }
                )
            }

            if (viewModel.mensajeError.isNotEmpty()) {
                Text(
                    text = viewModel.mensajeError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.agregarPaciente() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Registrar Paciente")
                }
                OutlinedButton(
                    onClick = { viewModel.limpiarFormulario() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // ---------- LISTA ----------
            Text(
                text = "Pacientes registrados (${viewModel.pacientes.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (viewModel.pacientes.isEmpty()) {
                Text("Aún no hay pacientes registrados.")
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.pacientes, key = { it.id }) { paciente ->
                    PacienteCard(paciente)
                }
            }
        }
    }
}

@Composable
fun PacienteCard(paciente: Paciente) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = paciente.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (paciente.esUrgencia) {
                    Text(
                        text = "🚨 URGENCIA",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text("Especie/Raza: ${paciente.especie}")
            Text("Edad: ${paciente.edad} años")
            Text("Contacto: ${paciente.contactoEmergencia}")
            Text(
                text = "ID: #${paciente.id}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PacienteCardPreview() {
    PacienteCard(
        Paciente(1, "Socrates", "Perro - Criollo", 2, "3001234567", true)
    )
}