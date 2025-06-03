package BD;

import java.sql.*;
import java.time.LocalDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class BD {

    public static Connection conectarBD(){

        Connection conexion;
        String host = "jdbc:mysql://localhost:3306/";
        String user= "root";
        String password = "mysql";
        String baseDeDatos = "Roguecito";

        System.out.println("Conectando a la base de datos...");

        try {
            conexion = DriverManager.getConnection(host+baseDeDatos, user, password);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return conexion;
    }


    public class GestorRecords {

        public static void insertarNuevoRecord(int puntuacion) {
            String sql = "INSERT INTO Records (nou_record, data_record) VALUES (?, ?)";

            try (Connection conn = BD.conectarBD();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, puntuacion);
                stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

                stmt.executeUpdate();
                System.out.println(" Récord guardado correctamente: " + puntuacion);

            } catch (SQLException e) {
                System.err.println(" Error al insertar el récord: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        conectarBD();
    }

}
