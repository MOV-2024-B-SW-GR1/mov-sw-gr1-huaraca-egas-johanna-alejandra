package org.example

import java.util.Date
fun main(args: Array<String>) {
    // INMUTABLES (No se RE ASIGNA "=")
    val inmutable: String = "Johanna Huaraca"
    // inmutable = "Vicente" // Error!

    // MUTABLES
    var mutable: String = "Johanna Huaraca"
    mutable = "Johanna Huaraca" // Ok

    // VAL > VAR

    // Duck Typing
    val ejemploVariable = " Johanna Huaraca "
    ejemploVariable.trim()

    val edadEjemplo: Int = 12
    // ejemploVariable = edadEjemplo // Error!

    // Variables Primitivas
    val nombreProfesor: String = "Johanna Huaraca"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    // Clases en Java
    val fechaNacimiento: Date = Date()

    // When (Switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        "C" -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }

    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito

    imprimirNombre("Johanna Huaraca")

    calcularSueldo(10.00) // solo parámetro requerido
    calcularSueldo(10.00, 15.00, 20.00) // parámetro requerido y sobreescribir parámetros opcionales

    // Named parameters
    calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parámetro bonoEspecial en 2da posición
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00) // usando parámetros nombrados
}

fun imprimirNombre(nombre: String): Unit { // Unit es opcional, es similar al void
    fun otraFuncionAdentro() {
        println("Otra función adentro")
    }
    println("Nombre: $nombre") // Uso sin llaves
    println("Nombre: ${nombre}") // Uso con llaves opcional
    println("Nombre: ${nombre + nombre}") // Uso con llaves (concatenado)
    println("Nombre: ${nombre.uppercase()}") // uso con llaves (función)
    otraFuncionAdentro()
}

fun calcularSueldo(
    sueldo: Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial: Double? = null // Opcional (nullable)
): Double {
    return if (bonoEspecial == null) {
        sueldo * (100 / tasa)
    } else {
        sueldo * (100 / tasa) * bonoEspecial
    }
}

abstract class Numeros(
    protected val numeroUno: Int,
    protected val numeroDos: Int,
    parametroNoUsadoNoPropiedadDeLaClase: Int? = null
) {
    init { // bloque constructor primario OPCIONAL
        println("Inicializando")
    }
}

class Suma(
    unoParametro: Int,
    dosParametro: Int
) : Numeros(unoParametro, dosParametro) {
    // Aquí puedes agregar métodos o propiedades específicas para la clase Suma
}