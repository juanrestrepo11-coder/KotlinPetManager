package com.utp.kotlinpetmanager.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utp.kotlinpetmanager.model.Paciente
import com.utp.kotlinpetmanager.ui.theme.KotlinPetManagerTheme
import com.utp.kotlinpetmanager.viewmodel.PacienteViewModel

// =====================================================
// PANTALLA PRINCIPAL
// =====================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteScreen(viewModel: PacienteViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("KotlinPet Manager 🐾", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Registro de pacientes",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Formulario
            item { FormularioRegistro(viewModel) }

            // 2. Encabezado de la lista
            item { EncabezadoLista(cantidad = viewModel.pacientes.size) }

            // 3. Lista de pacientes (o mensaje si está vacía)
            if (viewModel.pacientes.isEmpty()) {
                item { ListaVacia() }
            } else {
                items(viewModel.pacientes.reversed(), key = { it.id }) { paciente ->
                    PacienteCard(
                        paciente = paciente,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

// =====================================================
// FORMULARIO
// =====================================================
@Composable
fun FormularioRegistro(viewModel: PacienteViewModel) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Nuevo paciente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Completa los datos de la mascota",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CampoTexto(
                valor = viewModel.nombre,
                alCambiar = { viewModel.cambiarNombre(it) },
                etiqueta = "Nombre",
                icono = Icons.Filled.Pets
            )

            // Especie y edad lado a lado
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CampoTexto(
                    valor = viewModel.especie,
                    alCambiar = { viewModel.cambiarEspecie(it) },
                    etiqueta = "Especie / Raza",
                    icono = Icons.Filled.Category,
                    modifier = Modifier.weight(0.6f)
                )
                CampoTexto(
                    valor = viewModel.edad,
                    alCambiar = { viewModel.cambiarEdad(it) },
                    etiqueta = "Edad",
                    icono = Icons.Filled.Cake,
                    tipoTeclado = KeyboardType.Number,
                    modifier = Modifier.weight(0.4f)
                )
            }

            CampoTexto(
                valor = viewModel.contacto,
                alCambiar = { viewModel.cambiarContacto(it) },
                etiqueta = "Contacto de emergencia",
                icono = Icons.Filled.Phone,
                tipoTeclado = KeyboardType.Phone
            )

            SelectorUrgencia(
                activo = viewModel.esUrgencia,
                alCambiar = { viewModel.cambiarUrgencia(it) }
            )

            // Mensaje de error animado
            AnimatedVisibility(visible = viewModel.mensajeError.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = viewModel.mensajeError,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.agregarPaciente() },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Registrar Paciente", fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = { viewModel.limpiarFormulario() },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Limpiar formulario")
            }
        }
    }
}

// Campo de texto reutilizable (para no repetir el mismo código 4 veces)
@Composable
fun CampoTexto(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = modifier.fillMaxWidth()
    )
}

// Recuadro de urgencia que cambia de color al activarse
@Composable
fun SelectorUrgencia(activo: Boolean, alCambiar: (Boolean) -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (activo) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = if (activo) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("¿Es urgencia?", fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Requiere atención inmediata",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = activo,
                onCheckedChange = alCambiar,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

// =====================================================
// LISTA
// =====================================================
@Composable
fun EncabezadoLista(cantidad: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pacientes registrados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape
        ) {
            Text(
                text = "$cantidad",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ListaVacia() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Pets,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("Aún no hay pacientes", fontWeight = FontWeight.SemiBold)
        Text(
            text = "Los pacientes que registres aparecerán aquí",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PacienteCard(paciente: Paciente, modifier: Modifier = Modifier) {
    val colorAvatar = if (paciente.esUrgencia) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary
    val colorTextoAvatar = if (paciente.esUrgencia) MaterialTheme.colorScheme.onError
    else MaterialTheme.colorScheme.onPrimary

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = if (paciente.esUrgencia)
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        else CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con la inicial del nombre
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(colorAvatar, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = paciente.nombre.take(1).uppercase(),
                    color = colorTextoAvatar,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = paciente.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (paciente.esUrgencia) {
                        Surface(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "URGENTE",
                                color = MaterialTheme.colorScheme.onError,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "${paciente.especie} · Paciente #${paciente.id}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DatoConIcono(
                        icono = Icons.Filled.Cake,
                        texto = if (paciente.edad == 1) "1 año" else "${paciente.edad} años"
                    )
                    DatoConIcono(
                        icono = Icons.Filled.Phone,
                        texto = paciente.contactoEmergencia
                    )
                }
            }
        }
    }
}

@Composable
fun DatoConIcono(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(texto, style = MaterialTheme.typography.bodySmall)
    }
}

// =====================================================
// VISTAS PREVIAS
// =====================================================
@Preview(showBackground = true)
@Composable
fun PacienteCardPreview() {
    KotlinPetManagerTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PacienteCard(Paciente(1, "Socrates", "Perro - Criollo", 2, "3001234567", false))
            PacienteCard(Paciente(2, "Luna", "Gato - Siamés", 1, "3109876543", true))
        }
    }
}