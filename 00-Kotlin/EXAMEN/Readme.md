# EXAMEN / Profesores y Estudiantes

Este proyecto permite gestionar la información de profesores, estudiantes y materias en un sistema educativo. Los usuarios pueden agregar, actualizar, eliminar y consultar datos de profesores y estudiantes, así como asignar materias a los profesores y a los estudiantes. A través de un sistema de almacenamiento de archivos, los datos se mantienen persistentes.

## Descripción

El sistema permite gestionar estas entidades:

1. **Profesores**: Contienen información como el nombre, salario, fecha de inicio, si son tiempo completo y la materia que imparten.
2. **Estudiantes**: Contienen información sobre el nombre, calificación, fecha de inscripción, estado activo y el ID del profesor que los imparte.
2. **Curso**: El término "Curso" en este contexto corresponde a lo que en muchos sistemas educativos se denomina "Materia".


El sistema permite guardar, cargar y gestionar estos datos en archivos de texto planos, lo que facilita el almacenamiento persistente de la información.

## Características

- **Gestión de Profesores**: 
  - Los profesores tienen información sobre su nombre, salario, si son de tiempo completo, su fecha de inicio, y la materia que imparten.
  
- **Gestión de Estudiantes**: 
  - Los estudiantes tienen información sobre su nombre, calificación, fecha de inscripción, si están activos o no, y el ID de su profesor.

- **Gestión de Materias**: 
  - Cada materia (o curso) tiene información sobre su nombre, descripción, número de horas semanales, y los días en los que se imparte.
  - Los estudiantes se inscriben en las materias disponibles, y los profesores imparten estas materias.
  
  
- **Archivos de Texto**: 
  - El sistema guarda la información en tres archivos: `profesores.txt`, `estudiantes.txt`, `materias.txt`.
  
## Estructura de los Archivos

### 1. Archivo `profesores.txt`

Cada línea contiene un registro de un profesor con el siguiente formato:
```
ID;Nombre;Salario;EsTimeCompleto;FechaInicio;MateriaID
```
### 2. Archivo `estudiantes.txt`

Cada línea contiene un registro de un estudiante con el siguiente formato:
```
ID;Nombre;Calificacion;FechaInscripcion;Activo;ProfesorID
```
### 3. Archivo `materias.txt`

Cada línea contiene un registro de una materia (o curso) con el siguiente formato:
```
- **ID**: Un identificador único para la materia (entero).
- **Nombre**: El nombre de la materia (por ejemplo, "Matemáticas", "Historia", etc.).
- **Descripcion**: Una breve descripción de la materia (cadena de texto).
- **Horas**: El número de horas semanales que se dedica a la materia (entero).
- **Dias**: Los días de la semana en que se imparte la materia (cadena de texto). Se pueden separar los días con una coma, por ejemplo: `"Lunes, Miércoles"`.
**Importante**: En este sistema, el término **Materia** se refiere a lo que en muchos sistemas educativos se denomina **Curso**. Por lo tanto, un estudiante está inscrito en u
```

### Métodos

- **Lectura de archivos**: El programa lee los archivos de texto para cargar los datos de profesores, estudiantes y materias.
- **Escritura en archivos**: Los datos de los profesores, estudiantes y materias se guardan en los archivos de texto correspondientes.
- **Conversión de objetos**: Se utilizan métodos de conversión (`toString` y `fromString`) para convertir los objetos en cadenas de texto y viceversa.

## Uso del Sistema

El sistema está basado en un menú interactivo que permite al usuario realizar las siguientes acciones:

1. Crear un nuevo **Profesor**.
2. Crear un nuevo **Estudiante**.
3. Crear una nueva **Materia**.
4. Ver la lista de **Profesores**.
5. Ver la lista de **Estudiantes**.
6. Ver la lista de **Materias**.
7. Actualizar la información de un **Profesor**.
8. Actualizar la información de un **Estudiante**.
9. Actualizar la información de unA **Materia**.
10. Eliminar un **Profesor**.
11. Eliminar un **Estudiante**.
12. Eliminar una **Materia**.


## Uso

1. Clona o descarga el proyecto.
2. Ejecuta el archivo principal que contiene la lógica para manejar los profesores, estudiantes y materias.
3. Los datos se guardarán en los archivos `profesores.txt`, `estudiantes.txt` y `materias.txt` en la carpeta correspondiente.

## Requisitos

- Kotlin 1.5 o superior.
- JDK 11 o superior.
