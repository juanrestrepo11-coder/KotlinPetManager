package com.utp.kotlinpetmanager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.utp.kotlinpetmanager.model.Paciente

class PacienteViewModel : ViewModel() {

    // ---------- ESTADO DEL FORMULARIO ----------
    var nombre by mutableStateOf("")
        private set
    var especie by mutableStateOf("")
        private set
    var edad by mutableStateOf("")
        private set
    var contacto by mutableStateOf("")
        private set
    var esUrgencia by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf("")
        private set

    // ---------- LISTA DE PACIENTES ----------
    var pacientes by mutableStateOf(listOf<Paciente>())
        private set

    private var siguienteId = 1

    // ---------- EVENTOS (la vista avisa que algo cambió) ----------
    fun cambiarNombre(valor: String) { nombre = valor }
    fun cambiarEspecie(valor: String) { especie = valor }
    fun cambiarEdad(valor: String) { edad = valor }
    fun cambiarContacto(valor: String) { contacto = valor }
    fun cambiarUrgencia(valor: Boolean) { esUrgencia = valor }

    // ---------- LÓGICA ----------
    fun agregarPaciente() {
        // 1. Validar que no haya campos vacíos
        if (nombre.isBlank() || especie.isBlank() || edad.isBlank() || contacto.isBlank()) {
            mensajeError = "Por favor completa todos los campos"
            return
        }

        // 2. Validar que la edad sea un número entero válido
        val edadNumero = edad.trim().toIntOrNull()
        if (edadNumero == null || edadNumero < 0) {
            mensajeError = "La edad debe ser un número entero válido"
            return
        }

        // 3. Crear el paciente con el molde del Model
        val nuevo = Paciente(
            id = siguienteId,
            nombre = nombre.trim(),
            especie = especie.trim(),
            edad = edadNumero,
            contactoEmergencia = contacto.trim(),
            esUrgencia = esUrgencia
        )

        // 4. Agregarlo a la lista y preparar el siguiente id
        pacientes = pacientes + nuevo
        siguienteId++

        // 5. Dejar el formulario limpio para el siguiente registro
        limpiarFormulario()
    }

    fun limpiarFormulario() {
        nombre = ""
        especie = ""
        edad = ""
        contacto = ""
        esUrgencia = false
        mensajeError = ""
    }
}