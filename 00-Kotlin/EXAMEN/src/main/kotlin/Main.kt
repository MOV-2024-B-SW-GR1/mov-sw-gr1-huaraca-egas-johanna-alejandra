import javax.swing.JOptionPane
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
data class Materia(
    val id: Int,
    var nombre: String,
    var descripcion: String,
    var horas: Int,
    var dias: String
) {
    override fun toString(): String = "$id;$nombre;$descripcion;$horas;$dias"

    companion object {
        fun fromString(line: String): Materia {
            val parts = line.split(";")
            return Materia(
                parts[0].toInt(),
                parts[1],
                parts[2],
                parts[3].toInt(),
                parts[4]
            )
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
        fun fromString(line: String, materiasMap: Map<String, Int>): Profesor {
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
class GestorArchivos(private val rutaBase: String) {
    private val archivoProfesor = "${rutaBase}profesores.txt"
    private val archivoEstudiante = "${rutaBase}estudiantes.txt"
    private val archivoMateria = "${rutaBase}materias.txt"

    init {
        File(rutaBase).mkdirs()
        File(archivoProfesor).createNewFile()
        File(archivoEstudiante).createNewFile()
        File(archivoMateria).createNewFile()
    }

    // Materia operations
    fun obtenerSiguienteIdMateria(): Int {
        val materias = leerMaterias()
        return if (materias.isEmpty()) 1 else materias.maxOf { it.id } + 1
    }

    fun guardarMateria(materia: Materia) {
        val materias = leerMaterias().toMutableList()
        materias.add(materia)
        File(archivoMateria).writeText(materias.joinToString("\n"))
    }

    fun actualizarMateria(materia: Materia) {
        val materias = leerMaterias().toMutableList()
        val index = materias.indexOfFirst { it.id == materia.id }
        if (index != -1) {
            materias[index] = materia
            File(archivoMateria).writeText(materias.joinToString("\n"))
        }
    }

    fun eliminarMateria(id: Int) {
        val materias = leerMaterias().filter { it.id != id }
        File(archivoMateria).writeText(materias.joinToString("\n"))

        // También debemos manejar los profesores que tienen esta materia
        val profesores = leerProfesores().filter { it.materiaId != id }
        File(archivoProfesor).writeText(profesores.joinToString("\n"))
    }

    fun leerMaterias(): List<Materia> {
        return File(archivoMateria).readLines()
            .filter { it.isNotBlank() }
            .map { Materia.fromString(it) }
    }

    fun obtenerMateria(id: Int): Materia? {
        return leerMaterias().find { it.id == id }
    }

    // Profesor operations
    fun obtenerSiguienteIdProfesor(): Int {
        val profesores = leerProfesores()
        return if (profesores.isEmpty()) 1 else profesores.maxOf { it.id } + 1
    }

    fun guardarProfesor(profesor: Profesor) {
        val profesores = leerProfesores().toMutableList()
        profesores.add(profesor)
        File(archivoProfesor).writeText(profesores.joinToString("\n"))
    }

    fun actualizarProfesor(profesor: Profesor) {
        val profesores = leerProfesores().toMutableList()
        val index = profesores.indexOfFirst { it.id == profesor.id }
        if (index != -1) {
            profesores[index] = profesor
            File(archivoProfesor).writeText(profesores.joinToString("\n"))
        }
    }

    fun eliminarProfesor(id: Int) {
        val profesores = leerProfesores().filter { it.id != id }
        File(archivoProfesor).writeText(profesores.joinToString("\n"))

        // Eliminar estudiantes asociados al profesor
        val estudiantes = leerEstudiantes().filter { it.profesorId != id }
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun leerProfesores(): List<Profesor> {
        val materiasMap = leerMaterias().associate { it.nombre to it.id }
        return File(archivoProfesor).readLines()
            .filter { it.isNotBlank() }
            .map { Profesor.fromString(it, materiasMap) }
    }

    fun obtenerProfesor(id: Int): Profesor? {
        return leerProfesores().find { it.id == id }
    }

    // Estudiante operations
    fun obtenerSiguienteIdEstudiante(): Int {
        val estudiantes = leerEstudiantes()
        return if (estudiantes.isEmpty()) 1 else estudiantes.maxOf { it.id } + 1
    }

    fun guardarEstudiante(estudiante: Estudiante) {
        val estudiantes = leerEstudiantes().toMutableList()
        estudiantes.add(estudiante)
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun actualizarEstudiante(estudiante: Estudiante) {
        val estudiantes = leerEstudiantes().toMutableList()
        val index = estudiantes.indexOfFirst { it.id == estudiante.id }
        if (index != -1) {
            estudiantes[index] = estudiante
            File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
        }
    }

    fun eliminarEstudiante(id: Int) {
        val estudiantes = leerEstudiantes().filter { it.id != id }
        File(archivoEstudiante).writeText(estudiantes.joinToString("\n"))
    }

    fun leerEstudiantes(): List<Estudiante> {
        return File(archivoEstudiante).readLines()
            .filter { it.isNotBlank() }
            .map { Estudiante.fromString(it) }
    }

    fun obtenerEstudiante(id: Int): Estudiante? {
        return leerEstudiantes().find { it.id == id }
    }

    // Utility methods
    fun obtenerEstudiantesPorProfesor(profesorId: Int): List<Estudiante> {
        return leerEstudiantes().filter { it.profesorId == profesorId }
    }

    fun obtenerProfesoresPorMateria(materiaId: Int): List<Profesor> {
        return leerProfesores().filter { it.materiaId == materiaId }
    }

    fun existeMateria(id: Int): Boolean {
        return leerMaterias().any { it.id == id }
    }

    fun existeProfesor(id: Int): Boolean {
        return leerProfesores().any { it.id == id }
    }

    fun obtenerNombreMateria(id: Int): String? {
        return obtenerMateria(id)?.nombre
    }

    fun obtenerNombreProfesor(id: Int): String? {
        return obtenerProfesor(id)?.nombre
    }
}


object ValidationUtils {
    fun validarHoras(horasStr: String): Int {
        return try {
            val horas = horasStr.toInt()
            require(horas > 0) { "Las horas deben ser un número positivo" }
            horas
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Las horas deben ser un número válido")
        }
    }

    fun validarFecha(fecha: String): LocalDate {
        return try {
            val fechaParseada = LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE)
            require(!fechaParseada.isAfter(LocalDate.now())) { "La fecha no puede ser futura" }
            fechaParseada
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD")
        }
    }

    fun validarSalario(salarioStr: String): Double {
        return try {
            val salario = salarioStr.toDouble()
            require(salario > 0) { "El salario debe ser mayor que 0" }
            salario
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("El salario debe ser un número válido")
        }
    }

    fun validarCalificacion(calificacionStr: String): Double {
        return try {
            val calificacion = calificacionStr.toDouble()
            require(calificacion in 0.0..10.0) { "La calificación debe estar entre 0 y 10" }
            calificacion
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("La calificación debe ser un número válido")
        }
    }
}

class MateriaService(private val gestorArchivos: GestorArchivos) {
    fun crearMateria() {
        try {
            val id = gestorArchivos.obtenerSiguienteIdMateria()

            val nombre = JOptionPane.showInputDialog("Ingrese el nombre de la materia:")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val descripcion = JOptionPane.showInputDialog("Ingrese la descripción de la materia:")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("La descripción no puede estar vacía")

            val horasStr = JOptionPane.showInputDialog("Ingrese las horas semanales:")
            val horas = ValidationUtils.validarHoras(horasStr)

            val dias = JOptionPane.showInputDialog("Ingrese los días de clase:")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("Los días no pueden estar vacíos")

            val materia = Materia(id, nombre, descripcion, horas, dias)
            gestorArchivos.guardarMateria(materia)
            JOptionPane.showMessageDialog(null, "¡Materia creada exitosamente!")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error al crear materia: ${e.message}")
        }
    }

    fun actualizarMateria() {
        try {
            val materias = gestorArchivos.leerMaterias()
            if (materias.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay materias registradas")
                return
            }

            val materiaList = materias.map { "${it.id}: ${it.nombre}" }.toTypedArray()
            val materiaSeleccionada = JOptionPane.showInputDialog(
                null,
                "Seleccione la materia a actualizar:",
                "Selección de Materia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                materiaList,
                materiaList[0]
            ).toString()

            val materiaId = materiaSeleccionada.split(":")[0].toInt()
            val materia = materias.find { it.id == materiaId }!!

            val nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", materia.nombre)
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val descripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción:", materia.descripcion)
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("La descripción no puede estar vacía")

            val horasStr = JOptionPane.showInputDialog("Ingrese las nuevas horas semanales:", materia.horas)
            val horas = ValidationUtils.validarHoras(horasStr)

            val dias = JOptionPane.showInputDialog("Ingrese los nuevos días de clase:", materia.dias)
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("Los días no pueden estar vacíos")

            val materiaActualizada = Materia(materiaId, nombre, descripcion, horas, dias)
            gestorArchivos.actualizarMateria(materiaActualizada)
            JOptionPane.showMessageDialog(null, "¡Materia actualizada exitosamente!")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error al actualizar materia: ${e.message}")
        }
    }

    fun eliminarMateria() {
        try {
            val materias = gestorArchivos.leerMaterias()
            if (materias.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay materias registradas")
                return
            }

            val materiaList = materias.map { "${it.id}: ${it.nombre}" }.toTypedArray()
            val materiaSeleccionada = JOptionPane.showInputDialog(
                null,
                "Seleccione la materia a eliminar:",
                "Selección de Materia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                materiaList,
                materiaList[0]
            ).toString()

            val materiaId = materiaSeleccionada.split(":")[0].toInt()

            val confirmar = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar esta materia? Esto también eliminará las asignaciones de profesores.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
            )

            if (confirmar == JOptionPane.YES_OPTION) {
                gestorArchivos.eliminarMateria(materiaId)
                JOptionPane.showMessageDialog(null, "¡Materia eliminada exitosamente!")
            }
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error al eliminar materia: ${e.message}")
        }
    }

    fun mostrarMaterias() {
        val materias = gestorArchivos.leerMaterias()
        if (materias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias registradas")
            return
        }

        val materiaInfo = materias.joinToString("\n") { materia ->
            """
            ID: ${materia.id}
            Nombre: ${materia.nombre}
            Descripción: ${materia.descripcion}
            Horas Semanales: ${materia.horas}
            Días: ${materia.dias}
            ----------------------------------------
            """.trimIndent()
        }
        JOptionPane.showMessageDialog(null, materiaInfo)
    }
}
class ProfesorService(private val gestorArchivos: GestorArchivos) {
    fun crearProfesor() {
        try {
            val materias = gestorArchivos.leerMaterias()
            if (materias.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe crear al menos una materia primero")
                return
            }

            val id = gestorArchivos.obtenerSiguienteIdProfesor()

            val nombre = JOptionPane.showInputDialog("Ingrese el nombre del profesor:")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val salarioStr = JOptionPane.showInputDialog("Ingrese el salario del profesor:")
            val salario = ValidationUtils.validarSalario(salarioStr)

            val esTimeCompleto = JOptionPane.showConfirmDialog(
                null,
                "¿El profesor es de tiempo completo?",
                "Estado Laboral",
                JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION

            val fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de inicio (YYYY-MM-DD):")
            val fechaInicio = ValidationUtils.validarFecha(fechaStr)

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

    fun mostrarProfesores() {
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

    fun actualizarProfesor() {
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

            val nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", profesor.nombre)
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val salarioStr = JOptionPane.showInputDialog("Ingrese el nuevo salario:", profesor.salario)
            val salario = ValidationUtils.validarSalario(salarioStr)

            val esTimeCompleto = JOptionPane.showConfirmDialog(
                null,
                "¿El profesor es de tiempo completo?",
                "Estado Laboral",
                JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION

            val fechaStr = JOptionPane.showInputDialog(
                "Ingrese la nueva fecha de inicio (YYYY-MM-DD):",
                profesor.fechaInicio.toString()
            )
            val fechaInicio = ValidationUtils.validarFecha(fechaStr)

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

            val materiaId = materiaSeleccionada.split(":")[0].toInt()

            val profesorActualizado = Profesor(profesorId, nombre, salario, esTimeCompleto, fechaInicio, materiaId)
            gestorArchivos.actualizarProfesor(profesorActualizado)
            JOptionPane.showMessageDialog(null, "¡Profesor actualizado exitosamente!")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error al actualizar profesor: ${e.message}")
        }
    }

    fun eliminarProfesor() {
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
}


class EstudianteService(private val gestorArchivos: GestorArchivos) {
    fun crearEstudiante() {
        try {
            val profesores = gestorArchivos.leerProfesores()
            if (profesores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe crear al menos un profesor primero")
                return
            }

            val id = gestorArchivos.obtenerSiguienteIdEstudiante()

            val nombre = JOptionPane.showInputDialog("Ingrese el nombre del estudiante:")
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val calificacionStr = JOptionPane.showInputDialog("Ingrese la calificación del estudiante:")
            val calificacion = ValidationUtils.validarCalificacion(calificacionStr)

            val fechaStr = JOptionPane.showInputDialog("Ingrese la fecha de inscripción (YYYY-MM-DD):")
            val fechaInscripcion = ValidationUtils.validarFecha(fechaStr)

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

    fun mostrarEstudiantes() {
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

    fun actualizarEstudiante() {
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

            val nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:", estudiante.nombre)
                ?.takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("El nombre no puede estar vacío")

            val calificacionStr = JOptionPane.showInputDialog("Ingrese la nueva calificación:", estudiante.calificacion)
            val calificacion = ValidationUtils.validarCalificacion(calificacionStr)

            val fechaStr = JOptionPane.showInputDialog(
                "Ingrese la nueva fecha de inscripción (YYYY-MM-DD):",
                estudiante.fechaInscripcion.toString()
            )
            val fechaInscripcion = ValidationUtils.validarFecha(fechaStr)

            val activo = JOptionPane.showConfirmDialog(
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

            val profesorId = profesorSeleccionado.split(":")[0].toInt()

            val estudianteActualizado = Estudiante(estudianteId, nombre, calificacion, fechaInscripcion, activo, profesorId)
            gestorArchivos.actualizarEstudiante(estudianteActualizado)
            JOptionPane.showMessageDialog(null, "¡Estudiante actualizado exitosamente!")
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error al actualizar estudiante: ${e.message}")
        }
    }

    fun eliminarEstudiante() {
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
}




// Main.kt
fun main() {
    val gestorArchivos = GestorArchivos("C:\\Users\\johan\\OneDrive\\Documentos\\A\\Moviles\\mov-sw-gr1-huaraca-egas-johanna-alejandra\\00-Kotlin\\EXAMEN\\src\\main\\kotlin\\ARCHIVOTXT\\")
    val materiaService = MateriaService(gestorArchivos)
    val profesorService = ProfesorService(gestorArchivos)
    val estudianteService = EstudianteService(gestorArchivos)
    // ... otros servicios

    while (true) {
        val opcion = JOptionPane.showInputDialog(
            null,
            """
            Seleccione una opción:
            1. Gestión de Materias
            2. Gestión de Profesores
            3. Gestión de Estudiantes
            4. Salir
            """.trimIndent()
        )

        when (opcion) {
            "1" -> gestionarMaterias(materiaService)
            "2" -> gestionarProfesores(profesorService)
            "3" -> gestionarEstudiantes(estudianteService)
            "4" -> break
            else -> JOptionPane.showMessageDialog(null, "Opción inválida")
        }
    }
}

fun gestionarMaterias(materiaService: MateriaService) {
    val opcion = JOptionPane.showInputDialog(
        null,
        """
        Gestión de Materias:
        1. Crear Materia
        2. Ver Materias
        3. Actualizar Materia
        4. Eliminar Materia
        5. Volver al Menú Principal
        """.trimIndent()
    )

    when (opcion) {
        "1" -> materiaService.crearMateria()
        "2" -> materiaService.mostrarMaterias()
        "3" -> materiaService.actualizarMateria()
        "4" -> materiaService.eliminarMateria()
        "5" -> return
        else -> JOptionPane.showMessageDialog(null, "Opción inválida")
    }
}
fun gestionarProfesores(profesorService: ProfesorService) {
    val opcion = JOptionPane.showInputDialog(
        null,
        """
        Gestión de Profesores:
        1. Crear Profesor
        2. Ver Profesores
        3. Actualizar Profesor
        4. Eliminar Profesor
        5. Volver al Menú Principal
        """.trimIndent()
    )

    when (opcion) {
        "1" -> profesorService.crearProfesor()
        "2" -> profesorService.mostrarProfesores()
        "3" -> profesorService.actualizarProfesor()
        "4" -> profesorService.eliminarProfesor()
        "5" -> return
        else -> JOptionPane.showMessageDialog(null, "Opción inválida")
    }
}

fun gestionarEstudiantes(estudianteService: EstudianteService) {
    val opcion = JOptionPane.showInputDialog(
        null,
        """
        Gestión de Estudiantes:
        1. Crear Estudiante
        2. Ver Estudiantes
        3. Actualizar Estudiante
        4. Eliminar Estudiante
        5. Volver al Menú Principal
        """.trimIndent()
    )

    when (opcion) {
        "1" -> estudianteService.crearEstudiante()
        "2" -> estudianteService.mostrarEstudiantes()
        "3" -> estudianteService.actualizarEstudiante()
        "4" -> estudianteService.eliminarEstudiante()
        "5" -> return
        else -> JOptionPane.showMessageDialog(null, "Opción inválida")
    }
}
