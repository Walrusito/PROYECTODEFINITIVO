package proyectojava_DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
* @author Ricardo Delgado Vaquero
* @version 1.0
* @since 29/05/2024
*/
public class PartidaDAO {
    private static  Connection connection;

    public PartidaDAO(Connection connection) {
        this.connection = connection;
    }

    // CREA UNA NUEVA PARTIDA
    public static void crearPartida(int usuarioId1, int usuarioId2,Connection conexion) throws SQLException {
        String query = "INSERT INTO Partida (usuario_id_1, usuario_id_2) VALUES (?, ?)";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, usuarioId1);
            statement.setInt(2, usuarioId2);

            statement.executeUpdate();
            System.out.println("Partida creada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la partida: " + e.getMessage());
            throw e;
        }
    }
}