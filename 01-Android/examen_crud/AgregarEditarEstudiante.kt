package com.example.examen_crud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityAgregarEditarEstudiante : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var etNombreEstudiante: EditText
    private lateinit var etEdadEstudiante: EditText
    private lateinit var etEmailEstudiante: EditText
    private lateinit var etTelefonoEstudiante: EditText
    private lateinit var btnGuardarEstudiante: Button
    private var cursoId: Int = 0
    private var estudianteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_estudiante)

        controlador = Controlador(this)
        etNombreEstudiante = findViewById(R.id.etNombreEstudiante)
        etEdadEstudiante = findViewById(R.id.etEdadEstudiante)
        etEmailEstudiante = findViewById(R.id.etEmailEstudiante)
        etTelefonoEstudiante = findViewById(R.id.etTelefonoEstudiante)
        btnGuardarEstudiante = findViewById(R.id.btnGuardarEstudiante)
        val tvFormularioTitulo: TextView = findViewById(R.id.tvFormularioTitulo)

        cursoId = intent.getIntExtra("cursoId", 0)
        estudianteId = intent.getIntExtra("estudianteId", 0).takeIf { it != 0 }

        // Actualiza el título basado en si estamos editando o creando
        tvFormularioTitulo.text = if (estudianteId != null) "Editar Estudiante" else "Agregar Estudiante"

        // Cargar datos del estudiante si estamos editando
        if (estudianteId != null) {
            val estudiante = controlador.listarEstudiantesPorCurso(cursoId).find { it.id == estudianteId }
            estudiante?.let {
                etNombreEstudiante.setText(it.nombre)
                etEdadEstudiante.setText(it.edad.toString())
                etEmailEstudiante.setText(it.email)
                etTelefonoEstudiante.setText(it.telefono.toString())
            }
        }

        btnGuardarEstudiante.setOnClickListener {
            guardarEstudiante()
        }
    }

    private fun guardarEstudiante() {
        val nombre = etNombreEstudiante.text.toString()
        val edad = etEdadEstudiante.text.toString().toIntOrNull()
        val email = etEmailEstudiante.text.toString()
        val telefono = etTelefonoEstudiante.text.toString().toIntOrNull()

        if (nombre.isNotEmpty() && edad != null && email.isNotEmpty() && telefono != null) {
            if (estudianteId != null) {
                // Actualizar estudiante existente
                controlador.actualizarEstudiante(
                    Estudiante(estudianteId!!, nombre, edad, email, telefono)
                )
                Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show()
            } else {
                // Crear nuevo estudiante
                controlador.crearEstudiante(cursoId, Estudiante(0, nombre, edad, email, telefono))
                Toast.makeText(this, "Estudiante creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}
