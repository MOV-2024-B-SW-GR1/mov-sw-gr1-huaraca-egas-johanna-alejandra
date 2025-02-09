package com.example.examen_crud


import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityCursoList : AppCompatActivity() {
    private lateinit var controlador: Controlador
    private lateinit var listViewCursos: ListView
    private lateinit var btnAgregarCurso: Button
    private var selectedCurso: Curso? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curso_list)

        controlador = Controlador(this)
        listViewCursos = findViewById(R.id.listViewCursos)
        btnAgregarCurso = findViewById(R.id.btnAgregarCurso)

        btnAgregarCurso.setOnClickListener {
            val intent = Intent(this, ActivityAgregarEditarCurso::class.java)
            startActivity(intent)
        }

        registerForContextMenu(listViewCursos)
        actualizarLista()
    }

    private fun actualizarLista() {
        val cursos = controlador.listarCurso()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cursos.map { it.nombre })
        listViewCursos.adapter = adapter

        listViewCursos.setOnItemClickListener { _, _, position, _ ->
            selectedCurso = controlador.listarCurso()[position]
            listViewCursos.showContextMenu()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_curso, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuVerEstudiantes -> {
                val intent = Intent(this, ActivityEstudianteList::class.java)
                intent.putExtra("cursoId", selectedCurso?.id)
                startActivity(intent)
            }
            R.id.menuEditarCurso -> {
                val intent = Intent(this, ActivityAgregarEditarCurso::class.java)
                intent.putExtra("cursoId", selectedCurso?.id)
                startActivity(intent)
            }
            R.id.menuEliminarCurso -> {
                if (selectedCurso != null) {
                    controlador.eliminarCurso(selectedCurso!!.id)
                    Toast.makeText(this, "Curso eliminado", Toast.LENGTH_SHORT).show()
                    actualizarLista()
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }
}
