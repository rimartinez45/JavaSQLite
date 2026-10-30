# Sistema de Recaudo CDA

Aplicación de escritorio desarrollada en Java que permite gestionar los recaudos de un Centro de Diagnóstico Automotor (CDA), utilizando SQLite como sistema de almacenamiento de datos.

## Tecnologías utilizadas

- Java 17
- SQLite
- JDBC
- Maven
- Git y GitHub
- Visual Studio Code

## Funcionalidades

- Registrar recaudos.
- Almacenar información de placas y tipos de servicio.
- Registrar valores y medios de pago.
- Consultar todos los recaudos.
- Calcular el total recaudado.
- Consultar recaudos por fecha.
- Editar valores de recaudos.
- Eliminar registros con confirmación.
- Validar datos ingresados por el usuario.

## Estructura del proyecto

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

## Requisitos

- JDK 17 o superior.
- Apache Maven.
- Visual Studio Code o cualquier IDE compatible con Java.

## Ejecución

Clonar el repositorio:

```bash
git clone https://github.com/rimartinez45/JavaSQLite.git
```

Ingresar a la carpeta:

```bash
cd JavaSQLite
```

Compilar:

```bash
mvn compile
```

Ejecutar:

```bash
mvn exec:java "-Dexec.mainClass=RecaudoCDA" "-Dexec.classpathScope=runtime"
```

## Objetivo del proyecto

Proyecto práctico de aprendizaje y desarrollo de software, enfocado en aplicar programación orientada a objetos, operaciones CRUD, conexión a bases de datos SQLite y gestión de información mediante Java.

## Autor

**Richard Martinez**  
Ingeniero de Sistemas

GitHub: https://github.com/rimartinez45