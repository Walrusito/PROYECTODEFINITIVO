package proyectojava_DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import proyectojava.Juego;

//CAMBIOS PARA EL FUTURO:
//MAS INTERACCION DE USUARIO Y HISTORIAL
//MAS PERSONAJES CON MAS HABILIDADES (QUIZAS ARBOLES DE HABILIDAD)
//MEJORAR SISTEMA DE NIVELES
/**
* @author Ricardo Delgado Vaquero
* @version 1.0
* @since 29/05/2024
*/

public class HistorialDAO {
    private  Connection connection;

        public HistorialDAO(Connection connection) {
        this.connection = connection;
    }
    
    //GUARDA UNA PARTIDA EN EL HISTORIAL
        public static void guardarHistorial(int partidaId, int usuarioGanador, String faccionGanadora, Connection conexion) throws SQLException {
            String query = "INSERT INTO Historial ( usuario_ganador, faccion_ganadora) VALUES ( ?, ?)";

            try (PreparedStatement statement = conexion.prepareStatement(query)) {
                statement.setInt(1, usuarioGanador);
                statement.setString(2, faccionGanadora);

                statement.executeUpdate();
                System.out.println("Registro insertado en la tabla Historial correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al insertar registro en la tabla Historial: " + e.getMessage());
                throw e;
            }
        }
}


	



