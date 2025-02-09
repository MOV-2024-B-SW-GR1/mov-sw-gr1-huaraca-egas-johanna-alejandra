package com.example.examen_crud

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context?) : SQLiteOpenHelper(
    context,
    "CursosDB",
    null,
    4 // Versión incrementada por cambios en la estructura
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSqlCrearCurso = """
            CREATE TABLE Curso (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(250),
                descripcion VARCHAR(250),
                duracion INTEGER,
                latitude REAL,
                longitude REAL
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearCurso)

        val scriptSqlCrearEstudiante = """
            CREATE TABLE Estudiante (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(255),
                edad INTEGER,
                email VARCHAR(255),
                telefono VARCHAR(15),
                curso_id INTEGER,
                FOREIGN KEY (curso_id) REFERENCES Curso(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearEstudiante)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS Estudiante")
            db?.execSQL("DROP TABLE IF EXISTS Curso")
            onCreate(db)
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }
}
