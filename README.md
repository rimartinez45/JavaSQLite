# 💰 Sistema de Recaudos CDA

Aplicación de escritorio desarrollada en **Java 17** que permite gestionar los recaudos de un Centro de Diagnóstico Automotor (CDA), utilizando **SQLite** como sistema de almacenamiento de datos.

Este proyecto permite administrar operaciones de recaudo mediante funcionalidades de registro, consulta, actualización y eliminación de información.

## 🚀 Tecnologías utilizadas

- **Java 17:** lenguaje de programación.
- **SQLite:** sistema de gestión de bases de datos.
- **JDBC:** conexión entre Java y SQLite.
- **Maven:** gestión de dependencias y construcción del proyecto.
- **Git y GitHub:** control de versiones y alojamiento del código.
- **Visual Studio Code:** entorno de desarrollo.

## ⚙️ Funcionalidades

| Funcionalidad | Descripción |
|---|---|
| Registrar recaudos | Guardar nuevos recaudos en la base de datos. |
| Información del vehículo | Almacenar placa y tipo de servicio. |
| Datos del pago | Registrar valor y medio de pago. |
| Consultar recaudos | Visualizar todos los registros almacenados. |
| Total recaudado | Calcular la suma de los valores registrados. |
| Consulta por fecha | Buscar recaudos según una fecha determinada. |
| Editar recaudos | Actualizar el valor de un registro. |
| Eliminar recaudos | Eliminar registros solicitando confirmación. |
| Validación de datos | Controlar los datos ingresados por el usuario. |

## 📁 Estructura del proyecto

```text
JavaSQLite/
├── src/
│   └── main/
│       └── java/
│           └── RecaudoCDA.java
├── pom.xml
├── .gitignore
└── README.md
```

## 📋 Requisitos

Antes de ejecutar el proyecto necesitas:

- JDK 17 o superior.
- Apache Maven.
- Visual Studio Code o cualquier IDE compatible con Java.

## ▶️ Instalación y ejecución

**1. Clonar el repositorio**

```bash
git clone https://github.com/rimartinez45/JavaSQLite.git
```

**2. Ingresar a la carpeta del proyecto**

```bash
cd JavaSQLite
```

**3. Compilar el proyecto**

```bash
mvn compile
```

**4. Ejecutar la aplicación**

```bash
mvn exec:java "-Dexec.mainClass=RecaudoCDA" "-Dexec.classpathScope=runtime"
```

## 🎯 Objetivo del proyecto

Proyecto práctico de aprendizaje y desarrollo de software, orientado a aplicar programación en Java, manejo de bases de datos SQLite, operaciones CRUD, conexión mediante JDBC y gestión de información.

## 👨‍💻 Autor

**Richard Adrian Martínez Velásquez**

Ingeniero de Sistemas

- GitHub: [rimartinez45](https://github.com/rimartinez45)
- LinkedIn: [Richard Martínez](https://www.linkedin.com/in/richard-adrian-martinez-velasquez-17ab7024a/)
