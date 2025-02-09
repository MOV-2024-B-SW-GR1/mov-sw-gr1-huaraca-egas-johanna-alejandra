package com.example.examen_crud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navegar directamente a la lista de cursos
        val intent = Intent(this, ActivityCursoList::class.java)
        startActivity(intent)
    }
}