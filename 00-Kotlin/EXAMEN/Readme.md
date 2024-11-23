# EXAMEN / Profesores y Estudiantes

Este proyecto implementa un sistema sencillo de gestión para almacenar y manipular información de profesores, estudiantes y materias utilizando archivos de texto.

## Descripción

El sistema permite gestionar estas entidades:

1. **Profesores**: Contienen información como el nombre, salario, fecha de inicio, si son tiempo completo y la materia que imparten.
2. **Estudiantes**: Contienen información sobre el nombre, calificación, fecha de inscripción, estado activo y el ID del profesor que los imparte.


El sistema permite guardar, cargar y gestionar estos datos en archivos de texto planos, lo que facilita el almacenamiento persistente de la información.

## Características

- **Gestión de Profesores**: 
  - Los profesores tienen información sobre su nombre, salario, si son de tiempo completo, su fecha de inicio, y la materia que imparten.
  
- **Gestión de Estudiantes**: 
  - Los estudiantes tienen información sobre su nombre, calificación, fecha de inscripción, si están activos o no, y el ID de su profesor.
  
- **Archivos de Texto**: 
  - El sistema guarda la información en tres archivos: `profesores.txt`, `estudiantes.txt`.
  
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

### Métodos

- **Lectura de archivos**: El programa lee los archivos de texto para cargar los datos de profesores, estudiantes y materias.
- **Escritura en archivos**: Los datos de los profesores, estudiantes y materias se guardan en los archivos de texto correspondientes.
- **Conversión de objetos**: Se utilizan métodos de conversión (`toString` y `fromString`) para convertir los objetos en cadenas de texto y viceversa.

## Uso

1. Clona o descarga el proyecto.
2. Ejecuta el archivo principal que contiene la lógica para manejar los profesores, estudiantes y materias.
3. Los datos se guardarán en los archivos `profesores.txt`, `estudiantes.txt` y `materias.txt` en la carpeta correspondiente.

## Requisitos

- Kotlin 1.5 o superior.
- JDK 11 o superior.
