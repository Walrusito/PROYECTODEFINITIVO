package proyectojava;

//CAMBIOS PARA EL FUTURO:
//MAS INTERACCION DE USUARIO Y HISTORIAL
//MAS PERSONAJES CON MAS HABILIDADES (QUIZAS ARBOLES DE HABILIDAD)
//MEJORAR SISTEMA DE NIVELES
/**
 * @author Ricardo Delgado Vaquero
 * @version 1.0
 * @since 29/05/2024
 */

import java.sql.*;

import proyectojava_DAO.*;

public class App {

    public static void main(String[] args) {
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RESET = "\u001B[0m";

        Connection connection = null;
        //INICIA EL JUEGO Y MANTIENE EL BUCLE
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto", "root", "elnegosiososio");
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

            Usuario usuario = new Usuario();

            Usuario segundoUsuario = null;

            String entradaUsuario;
            do {
                Juego juego = new Juego(connection);
                segundoUsuario = juego.iniciarPartida(usuario.menuUsuarios(connection), connection, juego);
                System.out.println("Presione Enter para continuar jugando");
                entradaUsuario = new java.util.Scanner(System.in).nextLine(); 
            } while (entradaUsuario.isEmpty()); 

            System.out.println(ANSI_GREEN + "Gracias por jugar" + ANSI_RESET);

        } catch (SQLException e) {
            System.out.println("Error con la conexión a la base de datos");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}