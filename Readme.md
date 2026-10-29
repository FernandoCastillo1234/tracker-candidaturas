# Tracker de Candidaturas Laborales - v1.0

Sistema CLI para la gestión y seguimiento de candidaturas de empleo mediante almacenamiento en archivos de texto, junto con el prototipo de interfaz gráfica.

## Tecnologías Utilizadas
* Java 24
* Archivos de texto plano / CSV
* Git y GitHub
* Figma

## Instrucciones de Uso (CLI)

1. **Importar:** `java -cp src/main/java Main importar candidaturas.csv`
2. **Listar todas:** `java -cp src/main/java Main listar`
3. **Filtrar:** `java -cp src/main/java Main listar --estado pendiente --empresa Indra`
4. **Añadir:** `java -cp src/main/java Main añadir --titulo "Dev" --empresa "Sopra" --estado "pendiente" --fecha "15/10/2026"`
5. **Ver detalles:** `java -cp src/main/java Main ver 13`
6. **Borrar:** `java -cp src/main/java Main borrar 13`

## Tabla de Contribuciones v1.0

| Miembro      | Tareas Realizadas |
|:-------------| :--- |
| **Fernando** | Estructura CLI, comando `importar`, comando `añadir`, archivo `.gitignore`. |
| **Pedro**    | Dataset inicial, comandos `listar`, `ver`, `borrar` y manual de uso. |

## Diseño de Interfaces
* **Enlace al Prototipo de Figma:** [Ver Prototipo Interactivo en Figma](Tracker Candidaturas - Prototipo v1.0)
* **Archivo de Datos de Prueba:** [candidaturas.csv](./candidaturas.csv)