package proyectojava;

import proyectojava_DAO.*;

import java.sql.Connection;
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


public class Usuario {

	private static int contadorIds = 100;
	private int id;
	private String email;
	private String nickname;
	private String password;
	private Faccion faccion;
	private boolean logeado;
	private Personajes[] personajes;
	private int numeroVictoriasUsuario;
	private int nivel;

	// COLORINES//
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_RESET = "\u001B[0m";

	public Usuario() {
		this.id = contadorIds++;
	}

	public Usuario(String email, String nickname, String password, Faccion faccion) {
		this.id = contadorIds++;
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.faccion = faccion;
	}

	public Personajes[] getPersonajes() {
		return personajes;
	}

	public void setPersonajes(Personajes[] personajes) {
		this.personajes = personajes;
	}

	public String getEmail() {
		return email;
	}

	public boolean isLogeado() {
		return logeado;
	}

	public void setLogeado(boolean logeado) {
		this.logeado = logeado;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Faccion getFaccion() {
		return faccion;
	}

	public void setFaccion(Faccion faccion) {
		this.faccion = faccion;
	}

	public int getNumeroVictoriasUsuario() {
		return numeroVictoriasUsuario;
	}

	public void setNumeroVictoriasUsuario(int numeroVictoriasUsuario) {
		this.numeroVictoriasUsuario = numeroVictoriasUsuario;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	//MENU PRINCIPAL
	public Usuario menuUsuarios(Connection conexion) {
	    try {
	        switch (mostrarMenu()) {
	            case 1:
	                return crearUsuario(conexion);
	            case 2:
	                return login(conexion);
	        }
	    } catch (Exception e) {
	        System.out.println(ANSI_RED + "Error al seleccionar la opción del menú" + ANSI_RESET);
	        e.printStackTrace();
	    }
	    return null;
	}

	public int mostrarMenu() {
		int opcion;
		Scanner sc = new Scanner(System.in);
		System.out.println(ANSI_GREEN + "--------------------------------" + ANSI_RESET);
		System.out.println(ANSI_GREEN + "Pulse 1		CREAR CUENTA" + ANSI_RESET);
		System.out.println(ANSI_GREEN + "Pulse 2 	LOGIN" + ANSI_RESET);
		System.out.println(ANSI_GREEN + "--------------------------------" + ANSI_RESET);
		opcion = sc.nextInt();
		sc.nextLine();
		return opcion;

	}
	//METODO PARA CREAR USUARIOS
	public Usuario crearUsuario(Connection conexion) {
	    Scanner scanner = new Scanner(System.in);
	    String email;
	    boolean coinciden = false;

	    System.out.println(ANSI_CYAN + "Ingrese su email:" + ANSI_RESET);
	    email = scanner.nextLine();

	    coinciden = false;
	    System.out.println(ANSI_CYAN + "Ingrese su nickname:" + ANSI_RESET);
	    String nickname = scanner.nextLine();
	    System.out.println(ANSI_CYAN + "Ingrese su contraseña:" + ANSI_RESET);
	    String password = scanner.nextLine();
	    do {
	        System.out.println(ANSI_CYAN + "Ingrese de nuevo la contraseña" + ANSI_RESET);
	        String otraPassword = scanner.nextLine();
	        if (verificarContrasena(password, otraPassword)) {
	            coinciden = true;
	        } else {
	            System.out.println(ANSI_RED + "Las contraseñas no coinciden" + ANSI_RESET);
	        }
	    } while (!coinciden);
	    

	    System.out.println(ANSI_CYAN + "Seleccione su facción:" + ANSI_RESET);
	    System.out.println(ANSI_RED + "1. Fuego" + ANSI_RESET);
	    System.out.println(ANSI_BLUE + "2. Hielo" + ANSI_RESET);
	    System.out.println(ANSI_YELLOW + "3. Tierra" + ANSI_RESET);
	    System.out.println(ANSI_PURPLE + "4. Aire" + ANSI_RESET);
	    int opcion = -1;
	    try {
	        opcion = scanner.nextInt();
	        scanner.nextLine();
	    } catch (Exception e) {
	        System.out.println(ANSI_RED + "Error: Debe ingresar un número válido" + ANSI_RESET);
	        return null;
	    }
	    
	    Faccion faccion;
	    switch (opcion) {
	        case 1:
	            faccion = Faccion.Fuego;
	            break;
	        case 2:
	            faccion = Faccion.Hielo;
	            break;
	        case 3:
	            faccion = Faccion.Tierra;
	            break;
	        case 4:
	            faccion = Faccion.Aire;
	            break;
	        default:
	            System.out.println(ANSI_RED + "Error: Opción no válida" + ANSI_RESET);
	            return null;
	    }
	    
	    Usuario nuevoUsuario = new Usuario(email, nickname, password, faccion);
	    nuevoUsuario.setId(contadorIds++); 
	    try {
	        UsuarioDAO.agregarUsuario(nuevoUsuario, conexion);
	    } catch (SQLException e) {
	        System.out.println(ANSI_RED + "Error: Ya hay un usuario con ese email registrado" + ANSI_RESET);
	        e.printStackTrace();
	        return null;
	    }
	    nuevoUsuario.setLogeado(true);
	    System.out.println(ANSI_GREEN + "Usuario creado correctamente, sesión iniciada" + ANSI_RESET);
	    return nuevoUsuario;
	}
	//METODO QUE VERIFICA QUE AMBAS CONTRASEÑAS DEL REGISTRO SON IGUALES
	private boolean verificarContrasena(String contrasena, String otraContrasena) {
		return contrasena.equals(otraContrasena);
	}
	//METODO PARA HACER LOGIN
	public Usuario login(Connection conexion) {
	    Scanner sc = new Scanner(System.in);
	    UsuarioDAO auxiliar = new UsuarioDAO(conexion);

	    System.out.println(ANSI_CYAN + "Introduzca su email:" + ANSI_RESET);
	    String email = sc.nextLine();
	    System.out.println(ANSI_CYAN + "Introduzca su contraseña:" + ANSI_RESET);
	    String contrasenna = sc.nextLine();

	    try {
	        Usuario usuario = UsuarioDAO.verificarCredenciales(email, contrasenna, conexion);
	        if (usuario != null) {
	            usuario.setLogeado(true);
	            return usuario;
	        }
	    } catch (SQLException e) {
	        System.out.println(ANSI_RED + "Conexión fallida" + ANSI_RESET);
	        e.printStackTrace();
	    }
	    return null;
	}
	//OPCIONES DEL USUARIO
	public int opcionesUsuario(Usuario usuario, Usuario otroUsuario, Connection conexion) throws SQLException {
	    if(!usuario.isLogeado()) {
	        return 1;
	    }
	    int opcion;
	    Scanner sc = new Scanner(System.in);
	    System.out.println(ANSI_YELLOW + "Pulse 1 si desea cerrar sesión" + ANSI_RESET);
	    System.out.println(ANSI_RED + "Pulse 2 si desea eliminar su cuenta" + ANSI_RESET);
	    System.out.println(ANSI_PURPLE+"Pulse 3 si desea borrar su equipo"+ANSI_RESET);
	    System.out.println(ANSI_GREEN + "Pulse cualquier otro número para cambiar nickname y contraseña" + ANSI_RESET);

	    opcion = sc.nextInt();
	    switch (opcion) {
	    case 1:
	        usuario.setLogeado(false);
	        System.out.println(ANSI_GREEN+"Cerrando sesión"+ANSI_RESET);
	        break;
	    case 2:
	        try {
	            UsuarioDAO.borrarUsuario(otroUsuario, conexion);
	        } catch (SQLException e) {
	            System.out.println(ANSI_RED + "Error en la conexión" + ANSI_RESET);
	            e.printStackTrace();
	        }
	        break;
	    case 3:
	        ArrayList<Personajes> personajesList = UsuarioDAO.obtenerPersonajesUsuario(usuario, conexion);
	        if (personajesList == null || personajesList.isEmpty()) {
	            System.out.println(ANSI_GREEN + "No tienes ningún personaje para borrar." + ANSI_RESET);
	        } else {
	            for (Personajes personaje : personajesList) {
	                PersonajesDAO.deletePersonaje(personaje, conexion);
	            }
	            System.out.println(ANSI_GREEN + "Equipo borrado con éxito" + ANSI_RESET);
	        }
	        break;
	    case 4:
	        System.out.println(ANSI_CYAN + "Pulse 1 si desea cambiar su nickname" + ANSI_RESET);
	        System.out.println(ANSI_CYAN + "Pulse 2 si desea cambiar su contraseña" + ANSI_RESET);
	        int opcionActualizar = sc.nextInt();
	        sc.nextLine();
	        switch (opcionActualizar) {
	            case 1:
	                UsuarioDAO.actualizarPerfilUsuario(2, conexion,usuario );
	                System.out.println(ANSI_GREEN + "Nickname actualizado correctamente" + ANSI_RESET);
	                break;
	            case 2:
	                UsuarioDAO.actualizarPerfilUsuario(1, conexion,usuario);
	                System.out.println(ANSI_GREEN + "Contraseña actualizada correctamente" + ANSI_RESET);
	                break;
	            default:
	                System.out.println(ANSI_RED + "Opción inválida" + ANSI_RESET);
	                break;
	        }
	        break;
	    default:
	        return -1;
	    }
	    return opcion;
	}
}