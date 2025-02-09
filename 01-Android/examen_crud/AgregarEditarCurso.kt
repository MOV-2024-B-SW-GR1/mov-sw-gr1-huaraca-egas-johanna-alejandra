package com.example.examen_crud

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.examen_crud.MapActivity


class ActivityAgregarEditarCurso : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var etNombreCurso: EditText
    private lateinit var etDescripcionCurso: EditText
    private lateinit var etDuracionCurso: EditText
    private lateinit var btnGuardarCurso: Button
    private var cursoId: Int? = null

    // Variables para la ubicación
    private var latitude: Double? = null
    private var longitude: Double? = null

    companion object {
        private const val MAP_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_curso)

        controlador = Controlador(this)
        etNombreCurso = findViewById(R.id.etNombreCurso)
        etDescripcionCurso = findViewById(R.id.etDescripcionCurso)
        etDuracionCurso = findViewById(R.id.etDuracionCurso)
        btnGuardarCurso = findViewById(R.id.btnGuardarCurso)
        val tvFormularioTitulo: TextView = findViewById(R.id.tvFormularioTitulo)

        cursoId = intent.getIntExtra("cursoId", 0).takeIf { it != 0 }
        tvFormularioTitulo.text = if (cursoId != null) "Editar Curso" else "Agregar Curso"

        if (cursoId != null) {
            val curso = controlador.listarCurso().find { it.id == cursoId }
            curso?.let {
                etNombreCurso.setText(it.nombre)
                etDescripcionCurso.setText(it.descripcion)
                etDuracionCurso.setText(it.duracion.toString())
                latitude = it.latitude
                longitude = it.longitude
            }
        }

        btnGuardarCurso.setOnClickListener {
            val nombre = etNombreCurso.text.toString()
            val descripcion = etDescripcionCurso.text.toString()
            val duracion = etDuracionCurso.text.toString().toIntOrNull()

            if (nombre.isNotEmpty() && descripcion.isNotEmpty() && duracion != null) {
                if (cursoId != null) {
                    controlador.actualizarCurso(
                        Curso(cursoId!!, nombre, descripcion, duracion, latitude, longitude)
                    )
                    Toast.makeText(this, "Curso actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    val nuevoId = (controlador.listarCurso().maxOfOrNull { it.id } ?: 0) + 1
                    controlador.crearCurso(Curso(nuevoId, nombre, descripcion, duracion, latitude, longitude))
                    Toast.makeText(this, "Curso creado", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_location, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSelectLocation -> {
                val intent = Intent(this, MapActivity::class.java)
                if (latitude != null && longitude != null) {
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude", longitude)
                }
                intent.putExtra("viewMode", false)
                startActivityForResult(intent, MAP_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            latitude = data?.getDoubleExtra("latitude", 0.0)
            longitude = data?.getDoubleExtra("longitude", 0.0)
            Toast.makeText(this, "Ubicación seleccionada: ($latitude, $longitude)", Toast.LENGTH_SHORT).show()
        }
    }
}
