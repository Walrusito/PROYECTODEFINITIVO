package proyectojava_DAO;

import proyectojava.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

//CAMBIOS PARA EL FUTURO:
//MAS INTERACCION DE USUARIO Y HISTORIAL
//MAS PERSONAJES CON MAS HABILIDADES (QUIZAS ARBOLES DE HABILIDAD)
//MEJORAR SISTEMA DE NIVELES
/**
* @author Ricardo Delgado Vaquero
* @version 1.0
* @since 29/05/2024
*/

public class UsuarioDAO {
	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	// AGREGA UN USUARIO A LA BASE DE DATOS
	public static void agregarUsuario(Usuario usuario, Connection conexion) throws SQLException {
		String query = "INSERT INTO Usuarios (email, nickname, password, faccion) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = conexion.prepareStatement(query)) {
			statement.setString(1, usuario.getEmail());
			statement.setString(2, usuario.getNickname());
			statement.setString(3, usuario.getPassword());
			statement.setString(4, usuario.getFaccion().name());
			statement.executeUpdate();
		}
	}

	// BORRA UN USUARIO DE LA BASE DE DATOS
	public static void borrarUsuario(Usuario usuario, Connection conexion) throws SQLException {
		String query = "DELETE FROM Usuarios WHERE id = ?";

		try (PreparedStatement statement = conexion.prepareStatement(query)) {
			statement.setInt(1, usuario.getId());
			statement.executeUpdate();
			for (Personajes personaje : usuario.getPersonajes()) {
				PersonajesDAO.deletePersonaje(personaje, conexion);

			}
		}
	}

	// OBTENER LOS PERSONAJES ASOCIADOS A UN JUGADOR POR ID
	public static Personajes[] obtenerPersonajesUsuario(int idUsuario, Connection conexion) throws SQLException {
		ArrayList<Personajes> personajesUsuario = new ArrayList<>();

		String query = "{CALL GetPersonajesByUsuarioId(?)}";
		try (CallableStatement statement = conexion.prepareCall(query)) {
			statement.setInt(1, idUsuario);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					String tipoPersonaje = resultSet.getString("tipo");
					Personajes personaje;

					switch (tipoPersonaje.toLowerCase()) {
					case "guerrero":
						personaje = new Guerrero(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"));
						break;
					case "arquero":
						personaje = new Arquero(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"));
						break;
					case "mago":
						personaje = new Mago(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"),
								resultSet.getInt("fuerzaMagica"));
						break;
					case "medico":
						personaje = new Medico(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"),
								resultSet.getInt("fuerzaMagica"));
						break;
					default:
						throw new IllegalArgumentException("Tipo de personaje desconocido: " + tipoPersonaje);
					}

					personajesUsuario.add(personaje);
				}
			}
		}

		Personajes[] personajesArray = new Personajes[personajesUsuario.size()];
		personajesArray = personajesUsuario.toArray(personajesArray);

		return personajesArray;
	}

	// OBTENER LOS PERSONAJES ASOCIADOS A UN JUGADOR POR OBJETO USUARIO
	public static ArrayList<Personajes> obtenerPersonajesUsuario(Usuario usuario, Connection conexion)
			throws SQLException {
		String query = "CALL GetPersonajesByUsuarioId(?)";
		try (CallableStatement statement = conexion.prepareCall(query)) {
			statement.setInt(1, usuario.getId());
			try (ResultSet resultSet = statement.executeQuery()) {
				ArrayList<Personajes> personajesList = new ArrayList<>();
				while (resultSet.next()) {
					String tipoPersonaje = resultSet.getString("tipo_personaje");
					Personajes personaje;

					switch (tipoPersonaje.toLowerCase()) {
					case "guerrero":
						personaje = new Guerrero(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"));
						break;
					case "arquero":
						personaje = new Arquero(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"));
						break;
					case "mago":
						personaje = new Mago(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"),
								resultSet.getInt("fuerzaMagica"));
						break;
					case "medico":
						personaje = new Medico(resultSet.getString("nombrePersonaje"), resultSet.getInt("vida"),
								resultSet.getInt("estamina"), resultSet.getInt("fuerza"), resultSet.getInt("mana"),
								resultSet.getInt("fuerzaMagica"));
						break;
					default:
						throw new IllegalArgumentException("Tipo de personaje desconocido: " + tipoPersonaje);
					}
					personajesList.add(personaje);
				}
				if (personajesList.isEmpty()) {
					return null;
				} else {

					return personajesList;
				}
			}
		}
	}

	// BORRAR PERSONAJE POR ID
	public static void borrarUsuario(int idUsuario, Connection conexion) throws SQLException {

		Personajes[] personajesUsuario = obtenerPersonajesUsuario(idUsuario, conexion);

		String query = "DELETE FROM Usuarios WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(query)) {
			statement.setInt(1, idUsuario);
			statement.executeUpdate();

			for (Personajes personaje : personajesUsuario) {
				PersonajesDAO.deletePersonaje(personaje, conexion);
			}
			System.out.println("Personajes eliminados");
		}
	}

	// VERIFICAR PERSONAJES LLAMA A PROCEDIMIENTO
	public static Usuario verificarCredenciales(String email, String password, Connection conexion)
			throws SQLException {
		String mensaje = "";
		Usuario usuario = null;
		String query = "{CALL IniciarSesionUsuario(?, ?)}";

		try (CallableStatement statement = conexion.prepareCall(query)) {
			statement.setString(1, email);
			statement.setString(2, password);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					mensaje = resultSet.getString("mensaje");
					if (mensaje.equals("Inicio de sesión exitoso")) {
						String selectUserQuery = "SELECT * FROM Usuarios WHERE email = ?";
						try (PreparedStatement userStatement = conexion.prepareStatement(selectUserQuery)) {
							userStatement.setString(1, email);
							try (ResultSet userResultSet = userStatement.executeQuery()) {
								if (userResultSet.next()) {
									String nickname = userResultSet.getString("nickname");
									Faccion faccion = Faccion.valueOf(userResultSet.getString("faccion"));
									usuario = new Usuario(email, nickname, password, faccion);
								}
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al verificar credenciales: " + e.getMessage());
		}

		System.out.println(mensaje);
		return usuario;
	}

	// ACTUALIZA LAS VICTORIAS DE UN USUARIO
	public static void actualizarVictoriasUsuario(Usuario usuario, Connection conexion) throws SQLException {
		String sql = "UPDATE usuarios SET numeroVictoriasUsuario = ? WHERE id = ?";

		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			statement.setInt(1, usuario.getNumeroVictoriasUsuario());
			statement.setInt(2,usuario.getId());

			statement.executeUpdate();
		}
	}

	// VERIFICA SI EXISTE ALGUN CORREO EN LA BD CON UNA CONSULTA COUNT Y UN RETURN
	// BOOLEANO
	public boolean correoElectronicoExiste(String email, Connection conexion) throws SQLException {
		String query = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
		try (PreparedStatement statement = conexion.prepareStatement(query)) {
			statement.setString(1, email);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		}
		return false;
	}
		// CAMBIAR NICKNAME Y CONTRASEÑA DE UN USUARIO
		public static void actualizarPerfilUsuario(int opcion, Connection conexion, Usuario usuario) throws SQLException {
		    String query = "{CALL ActualizarPerfilUsuario(?, ?, ?)}";  // Agrega el parámetro id

		    try (Scanner scanner = new Scanner(System.in); CallableStatement statement = conexion.prepareCall(query)) {

		        statement.setInt(1, usuario.getId());

		        if (opcion == 1) {
		            System.out.println("Ingrese la nueva contraseña:");
		            String nuevaContraseña = scanner.nextLine();
		            statement.setString(2, usuario.getNickname());
		            statement.setString(3, nuevaContraseña);
		        } else if (opcion == 2) {
		            System.out.println("Ingrese el nuevo nickname:");
		            String nuevoNickname = scanner.nextLine();
		            statement.setString(2, nuevoNickname);
		            statement.setString(3, usuario.getPassword());
		        }

		        statement.executeUpdate();
		        System.out.println("Perfil de usuario actualizado correctamente.");
		    } catch (SQLException e) {
		        System.out.println("Error al actualizar el perfil del usuario: " + e.getMessage());
		    }
		}

}