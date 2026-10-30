

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RecaudoCDA {

    // Conexión para leer los datos ingresados por teclado
    static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {

        String url = "jdbc:sqlite:recaudos.db";

        try (Connection conexion = DriverManager.getConnection(url)) {

            crearTabla(conexion);

            int opcion;

            do {
                System.out.println("\n================================");
                System.out.println("       SISTEMA DE RECAUDO CDA");
                System.out.println("================================");
                System.out.println("1. Registrar recaudo");
                System.out.println("2. Consultar todos los recaudos");
                System.out.println("3. Mostrar total recaudado");
                System.out.println("4. Consultar recaudos por fecha");
                System.out.println("5. Editar recaudo");
                System.out.println("6. Eliminar recaudo");
                System.out.println("0. Salir");
                System.out.println("================================");
                System.out.print("Seleccione una opción: ");

                opcion = leerEntero();

                switch (opcion) {

                    case 1 -> registrarRecaudo(conexion);

                    case 2 -> consultarRecaudos(conexion);

                    case 3 -> mostrarTotal(conexion);

                    case 4 -> consultarPorFecha(conexion);

                    case 5 -> editarRecaudo(conexion);

                    case 6 -> eliminarRecaudo(conexion);

                    case 0 -> System.out.println(
                            "Programa finalizado.");

                    default -> System.out.println(
                            "Opción no válida.");
                }

            } while (opcion != 0);

        } catch (SQLException e) {

            System.out.println("Error con la base de datos: "
                    + e.getMessage());
        }
    }

    // CREAR TABLA Y AGREGAR COLUMNAS SI NO EXISTEN
    static void crearTabla(Connection conexion) throws SQLException {

        String sql = """
            CREATE TABLE IF NOT EXISTS recaudos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                valor INTEGER NOT NULL CHECK(valor >= 0),
                fecha_hora TEXT NOT NULL
            )
            """;

        try (Statement sentencia = conexion.createStatement()) {
            sentencia.executeUpdate(sql);
        }

        agregarColumna(conexion, "placa",
                "TEXT NOT NULL DEFAULT ''");

        agregarColumna(conexion, "tipo_servicio",
                "TEXT NOT NULL DEFAULT ''");

        agregarColumna(conexion, "medio_pago",
                "TEXT NOT NULL DEFAULT ''");

        System.out.println("Base de datos preparada.");
    }

    // VERIFICAR Y AGREGAR COLUMNAS
    static void agregarColumna( Connection conexion,String columna, String definicion) throws SQLException {

        boolean existe = false;

        String consulta = "PRAGMA table_info(recaudos)";

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(consulta)) {

            while (resultado.next()) {

                if (columna.equals(resultado.getString("name"))) {
                    existe = true;
                    break;
                }
            }
        }

        if (!existe) {

            String sql = "ALTER TABLE recaudos ADD COLUMN "
                    + columna + " " + definicion;

            try (Statement sentencia = conexion.createStatement()) {
                sentencia.executeUpdate(sql);
            }
        }
    }

    // REGISTRAR UN NUEVO RECAUDO
    static void registrarRecaudo(Connection conexion)
            throws SQLException {

        System.out.println("\n--- REGISTRAR RECAUDO ---");

        System.out.print("Placa del vehículo: ");
        String placa = teclado.nextLine().trim().toUpperCase();

        if (placa.isEmpty()) {
            System.out.println("La placa es obligatoria.");
        return;
        }

        if (!placa.matches("[A-Z]{3}[0-9]{3}")
                && !placa.matches("[A-Z]{3}[0-9]{2}[A-Z]")) {

            System.out.println("Formato de placa no válido.");
            return;
        }

        System.out.print("Tipo de servicio (RTM, peritaje u otro): ");
        String servicio = teclado.nextLine().trim();

        if (servicio.isEmpty()) {
            System.out.println("El servicio es obligatorio.");
            return;
        }

        System.out.print("Valor recibido en pesos: ");
        int valor = leerEntero();

        if (valor <= 0) {
            System.out.println(
                    "El valor debe ser mayor que cero.");
            return;
        }

        System.out.print(
                "Medio de pago (efectivo, transferencia, tarjeta): ");

        String medioPago = teclado.nextLine().trim();

        if (medioPago.isEmpty()) {
            System.out.println(
                    "El medio de pago es obligatorio.");
            return;
        }

        String fecha = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = """
            INSERT INTO recaudos
                (placa, tipo_servicio, valor, medio_pago, fecha_hora)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, placa);
            sentencia.setString(2, servicio);
            sentencia.setInt(3, valor);
            sentencia.setString(4, medioPago);
            sentencia.setString(5, fecha);

            sentencia.executeUpdate();

            System.out.println(
                    "\n¡Recaudo registrado correctamente!");
        }
    }

    // CONSULTAR TODOS LOS RECAUDOS
    static void consultarRecaudos(Connection conexion)
            throws SQLException {

        String sql = """
            SELECT id, placa, tipo_servicio, valor,
                   medio_pago, fecha_hora
            FROM recaudos
            ORDER BY id DESC
            """;

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {

            System.out.println("\n--- TODOS LOS RECAUDOS ---");

            boolean encontrado = false;

            while (resultado.next()) {

                encontrado = true;

                mostrarRegistro(resultado);
            }

            if (!encontrado) {
                System.out.println("No hay recaudos registrados.");
            }
        }
    }

    // MOSTRAR UN REGISTRO
    static void mostrarRegistro(ResultSet resultado)
            throws SQLException {

        System.out.println("------------------------------");
        System.out.println("ID: "
                + resultado.getInt("id"));

        System.out.println("Placa: "
                + resultado.getString("placa"));

        System.out.println("Servicio: "
                + resultado.getString("tipo_servicio"));

        System.out.println("Valor: $"
                + resultado.getInt("valor"));

        System.out.println("Medio de pago: "
                + resultado.getString("medio_pago"));

        System.out.println("Fecha: "
                + resultado.getString("fecha_hora"));
    }

    // MOSTRAR TOTAL RECAUDADO
    static void mostrarTotal(Connection conexion)
            throws SQLException {

        String sql = """
            SELECT COALESCE(SUM(valor), 0) AS total
            FROM recaudos
            """;

        try (Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {

            if (resultado.next()) {

                System.out.println("\n==============================");
                System.out.println("TOTAL RECAUDADO: $"
                        + resultado.getLong("total"));
                System.out.println("==============================");
            }
        }
    }

    // CONSULTAR RECAUDOS POR FECHA
    static void consultarPorFecha(Connection conexion)
            throws SQLException {

        System.out.println("\n--- CONSULTAR POR FECHA ---");

        System.out.print("Ingrese la fecha (AAAA-MM-DD): ");

        String fecha = teclado.nextLine().trim();

        try {
            LocalDate.parse(fecha);

        } catch (DateTimeParseException e) {

            System.out.println(
                    "Fecha inválida. Utilice el formato AAAA-MM-DD.");

            return;
        }

        String sql = """
            SELECT id, placa, tipo_servicio, valor,
                   medio_pago, fecha_hora
            FROM recaudos
            WHERE fecha_hora LIKE ?
            ORDER BY fecha_hora
            """;

        try (PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(1, fecha + "%");

            try (ResultSet resultado = sentencia.executeQuery()) {

                System.out.println(
                        "\nRECAUDOS DEL DÍA: " + fecha);

                boolean encontrado = false;
                long totalDia = 0;

                while (resultado.next()) {

                    encontrado = true;

                    mostrarRegistro(resultado);

                    totalDia += resultado.getLong("valor");
                }

                if (encontrado) {

                    System.out.println("------------------------------");
                    System.out.println("TOTAL DEL DÍA: $" + totalDia);

                } else {

                    System.out.println(
                            "No hay recaudos para esa fecha.");
                }
            }
        }
    }

    // LEER NÚMEROS ENTEROS
    static int leerEntero() {

        while (true) {

            String entrada = teclado.nextLine().trim();

            try {

                return Integer.parseInt(entrada);

            } catch (NumberFormatException e) {

                System.out.print(
                        "Ingrese un número entero válido: ");
            }
        }
    }

    
static void editarRecaudo(Connection conexion)
        throws SQLException {

    System.out.println("\n--- EDITAR RECAUDO ---");

    System.out.print("Ingrese el ID del recaudo: ");
    int id = leerEntero();

    // Primero buscamos el recaudo
    String consulta = """
        SELECT id, placa, tipo_servicio, valor
        FROM recaudos
        WHERE id = ?
        """;

    try (PreparedStatement sentencia =
                 conexion.prepareStatement(consulta)) {

        sentencia.setInt(1, id);

        try (ResultSet resultado = sentencia.executeQuery()) {

            if (!resultado.next()) {
                System.out.println(
                        "No existe un recaudo con ese ID.");
                return;
            }

            System.out.println("\nRecaudo encontrado:");
            System.out.println("ID: " + resultado.getInt("id"));
            System.out.println("Placa: "
                    + resultado.getString("placa"));
            System.out.println("Servicio: "
                    + resultado.getString("tipo_servicio"));
            System.out.println("Valor actual: $"
                    + resultado.getInt("valor"));
        }
    }

    // Solicitamos el nuevo valor
    System.out.print("\nIngrese el nuevo valor: ");
    int nuevoValor = leerEntero();

    if (nuevoValor <= 0) {
        System.out.println(
                "El nuevo valor debe ser mayor que cero.");
        return;
    }

    // Actualizamos el registro
    String sql = """
        UPDATE recaudos
        SET valor = ?
        WHERE id = ?
        """;

    try (PreparedStatement sentencia =
                 conexion.prepareStatement(sql)) {

        sentencia.setInt(1, nuevoValor);
        sentencia.setInt(2, id);

        int filasModificadas = sentencia.executeUpdate();

        if (filasModificadas > 0) {
            System.out.println(
                    "\n¡Recaudo actualizado correctamente!");
        } else {
            System.out.println(
                    "No se pudo actualizar el recaudo.");
        }
    }
}

static void eliminarRecaudo(Connection conexion)
        throws SQLException {

    System.out.println("\n--- ELIMINAR RECAUDO ---");

    System.out.print("Ingrese el ID del recaudo: ");
    int id = leerEntero();

    String consulta = """
        SELECT id, placa, valor, fecha_hora
        FROM recaudos
        WHERE id = ?
        """;

    try (PreparedStatement sentencia =
                 conexion.prepareStatement(consulta)) {

        sentencia.setInt(1, id);

        try (ResultSet resultado = sentencia.executeQuery()) {

            if (!resultado.next()) {
                System.out.println("No existe un recaudo con ese ID.");
                return;
            }

            System.out.println("\nRecaudo encontrado:");
            System.out.println("ID: " + resultado.getInt("id"));
            System.out.println("Placa: " + resultado.getString("placa"));
            System.out.println("Valor: $" + resultado.getInt("valor"));
            System.out.println("Fecha: " + resultado.getString("fecha_hora"));
        }
    }

    System.out.print("\n¿Está seguro de eliminarlo? (S/N): ");
    String confirmacion = teclado.nextLine().trim();

    if (!confirmacion.equalsIgnoreCase("S")) {
        System.out.println("Operación cancelada.");
        return;
    }

    String sql = """
        DELETE FROM recaudos
        WHERE id = ?
        """;

    try (PreparedStatement sentencia =
                 conexion.prepareStatement(sql)) {

        sentencia.setInt(1, id);

        int filasEliminadas = sentencia.executeUpdate();

        if (filasEliminadas > 0) {
            System.out.println("¡Recaudo eliminado correctamente!");
        } else {
            System.out.println("No se pudo eliminar el recaudo.");
        }
    }
}

}
