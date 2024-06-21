package proyectojava_DAO;

import proyectojava.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;

//CAMBIOS PARA EL FUTURO:
//MAS INTERACCION DE USUARIO Y HISTORIAL
//MAS PERSONAJES CON MAS HABILIDADES (QUIZAS ARBOLES DE HABILIDAD)
//MEJORAR SISTEMA DE NIVELES
/**
* @author Ricardo Delgado Vaquero
* @version 1.0
* @since 29/05/2024
*/

public class PersonajesDAO {
	protected static Connection connection;

	public PersonajesDAO() {
	}

	public PersonajesDAO(Connection connection) {
		this.connection = connection;
	}
	 // AGREGA UN NUEVO PERSONAJE A LA BASE DE DATOS
	public static void addPersonaje(Personajes personaje, Usuario usuario, Connection connection) throws SQLException {
	    String storedProcedure = "{CALL InsertarPersonaje(?, ?, ?, ?, ?, ?, ?)}";

	    try (CallableStatement statement = connection.prepareCall(storedProcedure)) {
	        statement.setString(1, personaje.getNombrePersonaje());
	        statement.setInt(2, personaje.getVida());
	        statement.setInt(3, personaje.getEstamina());
	        statement.setInt(4, personaje.getFuerza());
	        statement.setInt(5, personaje.getMana());


	        if (personaje instanceof Guerrero) {
	            statement.setString(6, "Guerrero");
	            statement.setNull(7, Types.INTEGER);
	        } else if (personaje instanceof Arquero) {
	            statement.setString(6, "Arquero");
	            statement.setNull(7, Types.INTEGER);
	        } else if (personaje instanceof Mago) {
	            statement.setString(6, "Mago");
	            statement.setInt(7, ((Mago) personaje).getFuerzaMagica());
	        } else if (personaje instanceof Medico) {
	            statement.setString(6, "Medico");
	            statement.setInt(7, ((Medico) personaje).getFuerzaMagica());
	        } else {
	            throw new IllegalArgumentException("Tipo de personaje no reconocido: " + personaje.getClass());
	        }

	        statement.executeUpdate();
	    }
	}

	//BORRA UN PERSONAJE DE LA BASE DE DATOS
	public static void deletePersonaje(Personajes personaje, Connection conexion) {
	    try {
	        int idPersonaje = personaje.getId();
	        String deleteQuery = "DELETE FROM Personajes WHERE id_personaje = ?";
	        try (PreparedStatement statement = conexion.prepareStatement(deleteQuery)) {
	            statement.setInt(1, idPersonaje);
	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Personaje eliminado con éxito.");
	            } else {
	                System.out.println("No se encontró ningún personaje asociados a ese usuario .");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	//OBTIENE EL TIPO DE UN PERSONAJE
	 public static String getTipo(Personajes personaje) {
	        if (personaje instanceof Mago) {
	            return "Mago";
	        } else if (personaje instanceof Arquero) {
	            return "Arquero";
	        } else if (personaje instanceof Guerrero) {
	            return "Guerrero";
	        } else if (personaje instanceof Medico) {
	            return "Medico";
	        } else {
	            return "Desconocido";
	        }
	    }
	





	
	


}