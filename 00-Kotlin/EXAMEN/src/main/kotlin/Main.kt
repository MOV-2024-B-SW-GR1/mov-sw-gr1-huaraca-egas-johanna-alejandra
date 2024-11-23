import javax.swing.JOptionPane
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class Materia(
    val id: Int,
    val nombre: String
) {
    override fun toString(): String = "$id;$nombre"

    companion object {
        fun fromString(line: String): Materia {
            val parts = line.split(";")
            return Materia(parts[0].toInt(), parts[1])
        }
    }
}

data class Profesor(
    val id: Int,
    var nombre: String,
    var salario: Double,
    var esTimeCompleto: Boolean,
    var fechaInicio: LocalDate,
    var materiaId: Int
) {
    override fun toString(): String {
        return "$id;$nombre;$salario;$esTimeCompleto;${fechaInicio.format(DateTimeFormatter.ISO_DATE)};$materiaId"
    }

    companion object {
        fun fromString(line: String): Profesor {
            val parts = line.split(";")
            return Profesor(
                parts[0].toInt(),
                parts[1],
                parts[2].toDouble(),
                parts[3].toBoolean(),
                LocalDate.parse(parts[4]),
                parts[5].toInt()
            )
        }
    }
}

data class Estudiante(
    val id: Int,
    var nombre: String,
    var calificacion: Double,
    var fechaInscripcion: LocalDate,
    var activo: Boolean,
    var profesorId: Int
) {
    override fun toString(): String {
        return "$id;$nombre;$calificacion;${fechaInscripcion.format(DateTimeFormatter.ISO_DATE)};$activo;$profesorId"
    }

    companion object {
        fun fromString(line: String): Estudiante {
            val parts = line.split(";")
            return Estudiante(
                parts[0].toInt(),
                parts[1],
                parts[2].toDouble(),
                LocalDate.parse(parts[3]),
                parts[4].toBoolean(),
                parts[5].toInt()
            )
        }
    }
}

class GestorArchivos {
    private val rutaBase = "C:\\Users\\johan\\OneDrive\\Documentos\\A\\Moviles\\mov-sw-gr1-huaraca-egas-johanna-alejandra\\00-Kotlin\\EXAMEN\\src\\main\\kotlin\\ARCHIVOTXT\\"
    private val archivoProfesor = "${rutaBase}profesores.txt"
    private val archivoEstudiante = "${rutaBase}estudiantes.txt"
    private val archivoMateria = "${rutaBase}materias.txt"

    init {
        File(rutaBase).mkdirs()
        File(archivoProfesor).createNewFile()
        File(archivoEstudiante).createNewFile()
        File(archivoMateria).createNewFile()
        inicializarMaterias()
    }

    private fun inicializarMaterias() {
        if (File(archivoMateria).readLines().isEmpty()) {
            val materiasPredefinidas = listOf(
                Materia(1, "Matemáticas"),
                Materia(2, "Física"),
                Materia(3, "Química"),
                Materia(4, "Biología"),
                Materia(5, "Historia")
            )
            File(archivoMateria).writeText(materiasPredefinidas.joinToString("\n"))
        }
    }

    fun obtenerSiguienteIdProfesor(): Int {
        val profesores = leerProfesores()
        return if (profesores.isEmpty()) 1 else profesores.maxOf { it.id } + 1
    }

    fun obtenerSiguienteIdEstudiante(): Int {
        val estudiantes = leerEstudiantes()
        return if (estudiantes.isEmpty()) 1 else estudiantes.maxOf { it.id } + 1
    }

    fun guardarProfesor(profesor: Profesor) {
        val profesores = leerProfesores().toMutableList()
        profesores.add(profesor)
        File(archivoProfesor).writeText(profesores.joinToString("\n"))
    }

    fun guardarEstudiante(estudiante: Estudiante) {
        val estudiantes = leerEstudiantes().toMutableList()
        estudiantes.add(estudiante)
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun leerMaterias(): List<Materia> {
        return File(archivoMateria).readLines()
            .filter { it.isNotBlank() }
            .map { Materia.fromString(it) }
    }

    fun leerProfesores(): List<Profesor> {
        return File(archivoProfesor).readLines()
            .filter { it.isNotBlank() }
            .map { Profesor.fromString(it) }
    }

    fun leerEstudiantes(): List<Estudiante> {
        return File(archivoEstudiante).readLines()
            .filter { it.isNotBlank() }
            .map { Estudiante.fromString(it) }
    }

    fun actualizarProfesor(profesor: Profesor) {
        val profesores = leerProfesores().toMutableList()
        val index = profesores.indexOfFirst { it.id == profesor.id }
        if (index != -1) {
            profesores[index] = profesor
            File(archivoProfesor).writeText(profesores.joinToString("\n"))
        }
    }

    fun actualizarEstudiante(estudiante: Estudiante) {
        val estudiantes = leerEstudiantes().toMutableList()
        val index = estudiantes.indexOfFirst { it.id == estudiante.id }
        if (index != -1) {
            estudiantes[index] = estudiante
            File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
        }
    }

    fun eliminarProfesor(id: Int) {
        val profesores = leerProfesores().filter { it.id != id }
        File(archivoProfesor).writeText(profesores.joinToString("\n"))
        val estudiantes = leerEstudiantes().filter { it.profesorId != id }
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun eliminarEstudiante(id: Int) {
        val estudiantes = leerEstudiantes().filter { it.id != id }
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun obtenerMateria(id: Int): Materia? {
        return leerMaterias().find { it.id == id }
    }

    fun obtenerProfesor(id: Int): Profesor? {
        return leerProfesores().find { it.id == id }
    }
}

fun validarFecha(fecha: String): LocalDate? {
    return try {
        val fechaParseada = LocalDate.parse(fecha)
        if (fechaParseada.isAfter(LocalDate.now())) {
            throw IllegalArgumentException("La fecha no puede ser futura")
        }
        fechaParseada
    } catch (e: DateTimeParseException) {
        null
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun validarSalario(salario: String): Double? {
    return try {
        val salarioNumerico = salario.toDouble()
        if (salarioNumerico <= 0) {
            throw IllegalArgumentException("El salario debe ser mayor que 0")
        }
        salarioNumerico
    } catch (e: NumberFormatException) {
        null
    }
}

fun main() {
    val gestorArchivos = GestorArchivos()

    while (true) {
        val opcion = JOptionPane.showInputDialog(
            null,
            """
            Seleccione una opción:
            1. Crear Profesor
            2. Crear Estudiante
            3. Ver Profesores
            4. Ver Estudiantes
            5. Actualizar Profesor
            6. Actualizar Estudiante
            7. Eliminar Profesor
            8. Eliminar Estudiante
            9. Salir
            """.trimIndent()
        )

        when (opcion) {
            "1" -> crearProfesor(gestorArchivos)
            "2" -> crearEstudiante(gestorArchivos)
            "3" -> leerProfesores(gestorArchivos)
            "4" -> leerEstudiantes(gestorArchivos)
            "5" -> actualizarProfesor(gestorArchivos)
            "6" -> actualizarEstudiante(gestorArchivos)
            "7" -> eliminarProfesor(gestorArchivos)
            "8" -> eliminarEstudiante(gestorArchivos)
            "9" -> break
            else -> JOptionPane.showMessageDialog(null, "Opción inválida")
        }
    }
}

fun crearProfesor(gestorArchivos: GestorArchivos) {
    try {
        val id = gestorArchivos.obtenerSiguienteIdProfesor()
        val nombre = JOptionPane.showInputDialog("Ingrese el nombre del profesor:")
        if (nombre.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre no puede estar vacío")
        }

        var salario: Double? = null
        while (salario == null) {
            val salarioStr = JOptionPane.showInputDialog("Ingrese el salario del profesor:")
            salario = validarSalario(salarioStr)
            if (salario == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un salario válido (número mayor que 0)")
            }
        }

        val esTimeCompleto = JOptionPane.showConfirmDialog(
            null,
            "¿El profesor es de tiempo completo?",
            "Estado Laboral",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION

        var fechaInicio: LocalDate? = null
        while (fechaInicio == null) {
            val fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de inicio (YYYY-MM-DD):")
            fechaInicio = validarFecha(fechaStr)
            if (fechaInicio == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha válida en formato YYYY-MM-DD")
            }
        }

        val materias = gestorArchivos.leerMaterias()
        val materiaList = materias.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val materiaSeleccionada = JOptionPane.showInputDialog(
            null,
            "Seleccione la materia:",
            "Selección de Materia",
            JOptionPane.QUESTION_MESSAGE,
            null,
            materiaList,
            materiaList[0]
        ).toString()

        val materiaId = materiaSeleccionada.split(":")[0].toInt()

        val profesor = Profesor(id, nombre, salario, esTimeCompleto, fechaInicio, materiaId)
        gestorArchivos.guardarProfesor(profesor)
        JOptionPane.showMessageDialog(null, "¡Profesor creado exitosamente!")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al crear profesor: ${e.message}")
    }
}

fun crearEstudiante(gestorArchivos: GestorArchivos) {
    try {
        val profesores = gestorArchivos.leerProfesores()
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡Por favor, cree un profesor primero!")
            return
        }

        val id = gestorArchivos.obtenerSiguienteIdEstudiante()
        val nombre = JOptionPane.showInputDialog("Ingrese el nombre del estudiante:")
        if (nombre.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre no puede estar vacío")
        }

        val calificacion = JOptionPane.showInputDialog("Ingrese la calificación del estudiante:").toDouble()

        var fechaInscripcion: LocalDate? = null
        while (fechaInscripcion == null) {
            val fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de inscripción (YYYY-MM-DD):")
            fechaInscripcion = validarFecha(fechaStr)
            if (fechaInscripcion == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha válida en formato YYYY-MM-DD")
            }
        }

        val activo = JOptionPane.showConfirmDialog(
            null,
            "¿El estudiante está activo?",
            "Estado de Actividad",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION

        val profesorList = profesores.map { profesor ->
            val materia = gestorArchivos.obtenerMateria(profesor.materiaId)
            "${profesor.id}: ${profesor.nombre} - ${materia?.nombre ?: "Sin materia"}"
        }.toTypedArray()

        val profesorSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el profesor:",
            "Selección de Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            profesorList,
            profesorList[0]
        ).toString()

        val profesorId = profesorSeleccionado.split(":")[0].toInt()

        val estudiante = Estudiante(id, nombre, calificacion, fechaInscripcion, activo, profesorId)
        gestorArchivos.guardarEstudiante(estudiante)
        JOptionPane.showMessageDialog(null, "¡Estudiante creado exitosamente!")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al crear estudiante: ${e.message}")
    }
}

fun leerProfesores(gestorArchivos: GestorArchivos) {
    val profesores = gestorArchivos.leerProfesores()
    if (profesores.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay profesores registrados")
        return
    }

    val profesorInfo = profesores.joinToString("\n") { profesor ->
        val materia = gestorArchivos.obtenerMateria(profesor.materiaId)
        """
        ID: ${profesor.id}
        Nombre: ${profesor.nombre}
        Salario: ${profesor.salario}
        Tiempo Completo: ${if (profesor.esTimeCompleto) "Sí" else "No"}
        Fecha de Inicio: ${profesor.fechaInicio}
        Materia: ${materia?.nombre ?: "Sin materia"}
        ----------------------------------------
        """.trimIndent()
    }
    JOptionPane.showMessageDialog(null, profesorInfo)
}

fun leerEstudiantes(gestorArchivos: GestorArchivos) {
    val estudiantes = gestorArchivos.leerEstudiantes()
    if (estudiantes.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay estudiantes registrados")
        return
    }

    val estudianteInfo = estudiantes.joinToString("\n") { estudiante ->
        val profesor = gestorArchivos.obtenerProfesor(estudiante.profesorId)
        val materia = profesor?.let { gestorArchivos.obtenerMateria(it.materiaId) }
        """
        ID: ${estudiante.id}
        Nombre: ${estudiante.nombre}
        Calificación: ${estudiante.calificacion}
        Fecha de Inscripción: ${estudiante.fechaInscripcion}
        Activo: ${if (estudiante.activo) "Sí" else "No"}
        Profesor: ${profesor?.nombre ?: "Sin profesor"}
        Materia: ${materia?.nombre ?: "Sin materia"}
        ----------------------------------------
        """.trimIndent()
    }
    JOptionPane.showMessageDialog(null, estudianteInfo)
}

fun actualizarProfesor(gestorArchivos: GestorArchivos) {
    try {
        val profesores = gestorArchivos.leerProfesores()
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados")
            return
        }

        val profesorList = profesores.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val profesorSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el profesor a actualizar:",
            "Selección de Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            profesorList,
            profesorList[0]
        ).toString()

        val profesorId = profesorSeleccionado.split(":")[0].toInt()
        val profesor = profesores.find { it.id == profesorId }!!

        profesor.nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", profesor.nombre)

        var salario: Double? = null
        while (salario == null) {
            val salarioStr = JOptionPane.showInputDialog("Ingrese el nuevo salario:", profesor.salario)
            salario = validarSalario(salarioStr)
            if (salario == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un salario válido (número mayor que 0)")
            }
        }
        profesor.salario = salario

        profesor.esTimeCompleto = JOptionPane.showConfirmDialog(
            null,
            "¿El profesor es de tiempo completo?",
            "Estado Laboral",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION

        var fechaInicio: LocalDate? = null
        while (fechaInicio == null) {
            val fechaStr = JOptionPane.showInputDialog("Ingrese la nueva fecha de inicio (YYYY-MM-DD):",
                profesor.fechaInicio.format(DateTimeFormatter.ISO_DATE))
            fechaInicio = validarFecha(fechaStr)
            if (fechaInicio == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha válida en formato YYYY-MM-DD")
            }
        }
        profesor.fechaInicio = fechaInicio

        val materias = gestorArchivos.leerMaterias()
        val materiaList = materias.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val materiaSeleccionada = JOptionPane.showInputDialog(
            null,
            "Seleccione la nueva materia:",
            "Selección de Materia",
            JOptionPane.QUESTION_MESSAGE,
            null,
            materiaList,
            materiaList[0]
        ).toString()

        profesor.materiaId = materiaSeleccionada.split(":")[0].toInt()

        gestorArchivos.actualizarProfesor(profesor)
        JOptionPane.showMessageDialog(null, "¡Profesor actualizado exitosamente!")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al actualizar profesor: ${e.message}")
    }
}

fun actualizarEstudiante(gestorArchivos: GestorArchivos) {
    try {
        val estudiantes = gestorArchivos.leerEstudiantes()
        if (estudiantes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay estudiantes registrados")
            return
        }

        val estudianteList = estudiantes.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val estudianteSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el estudiante a actualizar:",
            "Selección de Estudiante",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estudianteList,
            estudianteList[0]
        ).toString()

        val estudianteId = estudianteSeleccionado.split(":")[0].toInt()
        val estudiante = estudiantes.find { it.id == estudianteId }!!

        estudiante.nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", estudiante.nombre)
        estudiante.calificacion = JOptionPane.showInputDialog("Ingrese la nueva calificación:",
            estudiante.calificacion).toDouble()

        var fechaInscripcion: LocalDate? = null
        while (fechaInscripcion == null) {
            val fechaStr = JOptionPane.showInputDialog("Ingrese la nueva fecha de inscripción (YYYY-MM-DD):",
                estudiante.fechaInscripcion.format(DateTimeFormatter.ISO_DATE))
            fechaInscripcion = validarFecha(fechaStr)
            if (fechaInscripcion == null) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese una fecha válida en formato YYYY-MM-DD")
            }
        }
        estudiante.fechaInscripcion = fechaInscripcion

        estudiante.activo = JOptionPane.showConfirmDialog(
            null,
            "¿El estudiante está activo?",
            "Estado de Actividad",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION

        val profesores = gestorArchivos.leerProfesores()
        val profesorList = profesores.map { profesor ->
            val materia = gestorArchivos.obtenerMateria(profesor.materiaId)
            "${profesor.id}: ${profesor.nombre} - ${materia?.nombre ?: "Sin materia"}"
        }.toTypedArray()

        val profesorSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el nuevo profesor:",
            "Selección de Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            profesorList,
            profesorList[0]
        ).toString()

        estudiante.profesorId = profesorSeleccionado.split(":")[0].toInt()

        gestorArchivos.actualizarEstudiante(estudiante)
        JOptionPane.showMessageDialog(null, "¡Estudiante actualizado exitosamente!")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al actualizar estudiante: ${e.message}")
    }
}

fun eliminarProfesor(gestorArchivos: GestorArchivos) {
    try {
        val profesores = gestorArchivos.leerProfesores()
        if (profesores.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay profesores registrados")
            return
        }

        val profesorList = profesores.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val profesorSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el profesor a eliminar:",
            "Selección de Profesor",
            JOptionPane.QUESTION_MESSAGE,
            null,
            profesorList,
            profesorList[0]
        ).toString()

        val profesorId = profesorSeleccionado.split(":")[0].toInt()

        val confirmar = JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de que desea eliminar este profesor y todos sus estudiantes asociados?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION
        )

        if (confirmar == JOptionPane.YES_OPTION) {
            gestorArchivos.eliminarProfesor(profesorId)
            JOptionPane.showMessageDialog(null, "¡Profesor y estudiantes asociados eliminados exitosamente!")
        }
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al eliminar profesor: ${e.message}")
    }
}

fun eliminarEstudiante(gestorArchivos: GestorArchivos) {
    try {
        val estudiantes = gestorArchivos.leerEstudiantes()
        if (estudiantes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay estudiantes registrados")
            return
        }

        val estudianteList = estudiantes.map { "${it.id}: ${it.nombre}" }.toTypedArray()
        val estudianteSeleccionado = JOptionPane.showInputDialog(
            null,
            "Seleccione el estudiante a eliminar:",
            "Selección de Estudiante",
            JOptionPane.QUESTION_MESSAGE,
            null,
            estudianteList,
            estudianteList[0]
        ).toString()

        val estudianteId = estudianteSeleccionado.split(":")[0].toInt()

        val confirmar = JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de que desea eliminar este estudiante?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION
        )

        if (confirmar == JOptionPane.YES_OPTION) {
            gestorArchivos.eliminarEstudiante(estudianteId)
            JOptionPane.showMessageDialog(null, "¡Estudiante eliminado exitosamente!")
        }
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Error al eliminar estudiante: ${e.message}")
    }
}