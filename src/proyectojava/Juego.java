package proyectojava;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import proyectojava_DAO.*;

//CAMBIOS PARA EL FUTURO:
//MAS INTERACCION DE USUARIO Y HISTORIAL
//MAS PERSONAJES CON MAS HABILIDADES (QUIZAS ARBOLES DE HABILIDAD)
//MEJORAR SISTEMA DE NIVELES
/**
* @author Ricardo Delgado Vaquero
* @version 1.0
* @since 29/05/2024
*/

public class Juego {
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

	private Connection conexion;
	public static int idPartida = 0;
	private static int contadorId = 0;
	private static Usuario IA = new Usuario();
	private static ArrayList<Personajes> PERSONAJES_IA = new ArrayList<>(
			Arrays.asList(new Mago("Merlín"), new Medico("Dr House"), new Guerrero("Kratos"), new Arquero("Legolas")));

	public Juego(Connection conexion) {
		this.conexion = conexion;
		this.idPartida=contadorId++;
	}
	
	//METODO DONDE SE DECIDE QUE TIPO DE PARTIDA JUGAR
	public Usuario iniciarPartida(Usuario usuarioPrincipio, Connection conexion, Juego juego) throws SQLException {
	    if (usuarioPrincipio == null) {
	        Usuario auxiliar = new Usuario();
	        usuarioPrincipio = juego.iniciarPartida(auxiliar.menuUsuarios(conexion), conexion, juego);
	    }
	    boolean opcionSeleccionada = false;
	    final int TAMAÑO_MAXIMO = 3;
	    Personajes[] jugador1 = new Personajes[TAMAÑO_MAXIMO];
	    Personajes[] jugador2 = new Personajes[TAMAÑO_MAXIMO];
	    System.out.println(ANSI_YELLOW + "No hay un segundo jugador. ¿Desea jugar en modo individual?" + ANSI_RESET);
	    System.out.println(ANSI_CYAN + "1. Sí" + ANSI_RESET);
	    System.out.println(ANSI_CYAN + "2. Registrar nueva cuenta y jugar al modo 1vs1" + ANSI_RESET);
	    System.out.println(ANSI_CYAN + "3. Opciones" + ANSI_RESET);

	    Scanner sc = new Scanner(System.in);
	    int opcion;
	    do {
	        opcion = sc.nextInt();
	        sc.nextLine();
	        switch (opcion) {
	            case 1:
	                System.out.println(ANSI_GREEN + "Ha entrado al modo individual (vs IA)" + ANSI_RESET);
	                modoIndividual(usuarioPrincipio, jugador1, jugador2, IA, PERSONAJES_IA, conexion);
	                opcionSeleccionada = true;
	                break;
	            case 2:
	                Usuario usuarioOpcional = new Usuario();
				Usuario segundoJugador = usuarioOpcional.menuUsuarios(conexion);
				modoPVP(usuarioPrincipio, segundoJugador, conexion);
	                opcionSeleccionada = true;
	                break;
	            case 3:
	            	Usuario auxiliar=new Usuario();
	                auxiliar.opcionesUsuario(usuarioPrincipio, null, conexion);
	                opcionSeleccionada = true;
	                break;
	            default:
	                System.out.println(ANSI_RED + "Opción inválida" + ANSI_RESET);
	        }
	    } while (!opcionSeleccionada);
	    return usuarioPrincipio;
	}
	//METODO PARA EL MODO PVP
	private void modoPVP(Usuario usuario1, Usuario usuario2, Connection conexion) {
	    Personajes[] personajesJugador1 = null;
	    Personajes[] personajesJugador2 = null;

	    try {
	        // Obtener los personajes de los usuarios
	        ArrayList<Personajes> listaPersonajesUsuario1 = UsuarioDAO.obtenerPersonajesUsuario(usuario1, conexion);
	        ArrayList<Personajes> listaPersonajesUsuario2 = UsuarioDAO.obtenerPersonajesUsuario(usuario2, conexion);

	        // Si no hay personajes para alguno de los usuarios, permitirles elegir su equipo
	        if (listaPersonajesUsuario1 != null && !listaPersonajesUsuario1.isEmpty()) {
	            personajesJugador1 = listaPersonajesUsuario1.toArray(new Personajes[0]);
	        } else {
	            System.out.println(usuario1.getNickname() + " deberá seleccionar a su equipo.");
	            personajesJugador1 = elegirEquipo(usuario1, conexion);
	        }

	        if (listaPersonajesUsuario2 != null && !listaPersonajesUsuario2.isEmpty()) {
	            personajesJugador2 = listaPersonajesUsuario2.toArray(new Personajes[0]);
	        } else {
	            System.out.println(usuario2.getNickname() + " deberá seleccionar a su equipo.");
	            personajesJugador2 = elegirEquipo(usuario2, conexion);
	        }

	        // Crear la partida en la base de datos
	       PartidaDAO.crearPartida(usuario1.getId(), usuario2.getId(), conexion);

	        // Mostrar pantalla de versus y empezar el juego
	        pantallaVersus(personajesJugador1, personajesJugador2);
	        empezarJuego(personajesJugador1, personajesJugador2, usuario1, usuario2);
	    } catch (SQLException e) {
	        System.out.println("Error al obtener los personajes de los usuarios: " + e.getMessage());
	    }
	}
	//METODO PARA EL MODO INDIVIDUAL
	private void modoIndividual(Usuario usuario1, Personajes[] jugador1, Personajes[] personajesIA, Usuario usuarioIA,
	        ArrayList<Personajes> tiposPersonaje, Connection conexion) throws SQLException {
	    Random random = new Random();
	    ArrayList<Personajes> listaPersonajesUsuario1 = UsuarioDAO.obtenerPersonajesUsuario(usuario1, conexion);

	    if (listaPersonajesUsuario1 == null || listaPersonajesUsuario1.isEmpty()) {
	        System.out.println(usuario1.getNickname() + " deberá seleccionar a su equipo.");
	        jugador1 = elegirEquipo(usuario1, conexion);
	    } else {
	        jugador1 = listaPersonajesUsuario1.toArray(new Personajes[0]);
	    }

	    for (int i = 0; i < personajesIA.length; i++) {
	        int indiceAleatorio = random.nextInt(tiposPersonaje.size());
	        Personajes personajeAleatorio = tiposPersonaje.get(indiceAleatorio);
	        personajesIA[i] = personajeAleatorio;
	    }

	    // Crear la partida en la base de datos
	    PartidaDAO.crearPartida(usuario1.getId(), usuarioIA.getId(), conexion);

	    // Mostrar pantalla de versus y empezar el juego
	    pantallaVersus(jugador1, personajesIA);
	    empezarJuego(jugador1, personajesIA, usuario1, usuarioIA);
	}
	//METODO PARA ELEGIR EQUIPO
	private Personajes[] elegirEquipo(Usuario usuario, Connection conexion) throws SQLException {
		Personajes[] jugador = new Personajes[3]; // Crear un array de personajes con tamaño 3
		System.out.println(ANSI_CYAN + "Elija los personajes del jugador" + ANSI_RESET);
		for (int i = 0; i < jugador.length; i++) {
			System.out.println(ANSI_CYAN + (i + 1) + " º personaje del equipo (3 por equipo)" + ANSI_RESET);
			switch (elegirPersonaje()) {
			case 1:
				Guerrero guerrero = new Guerrero(nombrePersonaje());
				PersonajesDAO.addPersonaje(guerrero, usuario, conexion);
				jugador[i] = guerrero;
				break;
			case 2:
				Arquero arquero = new Arquero(nombrePersonaje());
				PersonajesDAO.addPersonaje(arquero, usuario, conexion);
				jugador[i] = arquero;
				break;
			case 3:
				Medico medico = new Medico(nombrePersonaje());
				PersonajesDAO.addPersonaje(medico, usuario, conexion);
				jugador[i] = medico;
				break;
			case 4:
				Mago mago = new Mago(nombrePersonaje());
				PersonajesDAO.addPersonaje(mago, usuario, conexion);
				jugador[i] = mago;
				break;
			default:
				System.out.println(ANSI_RED + "Opción no válida" + ANSI_RESET);
				i--;
				break;
			}
		}
		return jugador;
	}
	//METODO PARA ELEGIR A LOS DIFERENTES PERSONAJES
	private int elegirPersonaje() {
		int opcion = 0;
		Scanner sc = new Scanner(System.in);

		try {
			System.out.println(ANSI_YELLOW + "1.- GUERRERO, 2.- ARQUERO, 3.- MEDICO, 4.- MAGO" + ANSI_RESET);
			opcion = sc.nextInt();
			sc.nextLine();

			if (opcion < 1 || opcion > 4) {
				throw new IllegalArgumentException("Opción inválida, seleccione un número entre 1 y 4.");
			}

		} catch (InputMismatchException e) {
			System.out.println(ANSI_RED + "Error: Debe ingresar un número." + ANSI_RESET);
			sc.nextLine();
		} catch (IllegalArgumentException e) {
			System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
		}

		return opcion;
	}
	//METODO PARA QUE EL JUGADOR INTRODUZCA EK NOMBRE DEL PERSONAJE
	private String nombrePersonaje() {
		Scanner sc = new Scanner(System.in);
		System.out.println(ANSI_GREEN + "Introduzca el nombre de su personaje" + ANSI_RESET);
		String nombre = sc.nextLine();
		return nombre;
	}
	//METODO PARA IMPRIMIR UNA PANTALLA DE VS POR CONSOLA
	private void pantallaVersus(Personajes[] jugador1, Personajes[] jugador2) {
		System.out.println(ANSI_CYAN + "-----------------EQUIPO 1-----------------" + ANSI_RESET);
		Personajes.mostrarJugadores(jugador1);
		System.out.println(" ");
		System.out.println(ANSI_GREEN + "--------------------VS--------------------" + ANSI_RESET);
		System.out.println(" ");
		System.out.println(ANSI_RED + "-----------------EQUIPO 2-----------------" + ANSI_RESET);
		Personajes.mostrarJugadores(jugador2);
		System.out.println(" ");
		System.out.println(" ");
		;
	}
	//METODO QUE INICIA EL JUEGO
	private void empezarJuego(Personajes[] jugador1, Personajes[] jugador2, Usuario primerUsuario,
			Usuario segundoUsuario) {
		boolean haPerdido = false;
		int opcion;
		do {
			if (!haPerdido) {
				for (Personajes personaje : jugador1) {
					if (!Personajes.hayPersonajesConVida(jugador2)) {
						System.out.println(ANSI_GREEN + "ENHORABUENA " + primerUsuario.getNickname() + ", HAS GANADO"
								+ ANSI_RESET);
						for(Personajes personajeRestablecer:jugador1) {
							personaje.restablecerEstadisticasPersonaje(personajeRestablecer);
						}
						for(Personajes personajeRestablecer:jugador2) {
							personaje.restablecerEstadisticasPersonaje(personajeRestablecer);
						}
						incrementarVictoriasJugador(primerUsuario);
						subirNivel(primerUsuario);
						añadirAlHistorial(primerUsuario.getId(), segundoUsuario.getId(), primerUsuario.getFaccion().toString(), primerUsuario.getId(), conexion);
						haPerdido = true;
						break;
					}
					if (personaje.getVida() > 0) {
						System.out.println(ANSI_WHITE + "A quién quieres atacar?" + ANSI_RESET);
						opcion = personaje.elegirAtaque(personaje);
						switch (opcion) {
						case 1:
							personaje.ataqueLigero(jugador2[Personajes.elegirAtacado(jugador2) - 1], personaje);
							break;
						case 2:
							personaje.ataquePesado(jugador2[Personajes.elegirAtacado(jugador2) - 1], personaje);
							break;
						case 3:
							if (personaje instanceof Medico) {
								personaje.usarHabilidad(jugador1, jugador2, personaje);
							} else if (personaje instanceof Mago) {
								personaje.usarHabilidad(jugador1, jugador2, personaje);
							} else {
								personaje.usarHabilidad();
							}
						}
					} else {
						System.out
								.println(personaje.getNombrePersonaje() + ANSI_WHITE + ", no tiene vida" + ANSI_RESET);
					}
				}
			}

			if (!haPerdido) {
				for (Personajes personaje : jugador2) {
					if (!Personajes.hayPersonajesConVida(jugador1)) {
						System.out.println(ANSI_GREEN + "ENHORABUENA " + segundoUsuario.getNickname() + ", HAS GANADO"
								+ ANSI_RESET);
						for(Personajes personajeRestablecer:jugador1) {
							personaje.restablecerEstadisticasPersonaje(personajeRestablecer);
						}
						for(Personajes personajeRestablecer:jugador2) {
							personaje.restablecerEstadisticasPersonaje(personajeRestablecer);
						}
						incrementarVictoriasJugador(segundoUsuario);
						subirNivel(segundoUsuario);
						añadirAlHistorial(primerUsuario.getId(), segundoUsuario.getId(), segundoUsuario.getFaccion().toString(), segundoUsuario.getId(), conexion);
						haPerdido = true;
						break;
					}
					if (personaje.getVida() > 0) {
						System.out.println(ANSI_WHITE + "A quién quieres atacar?" + ANSI_RESET);
						opcion = personaje.elegirAtaque(personaje);
						switch (opcion) {
						case 1:
							personaje.ataqueLigero(jugador1[Personajes.elegirAtacado(jugador1) - 1], personaje);
							break;
						case 2:
							personaje.ataquePesado(jugador1[Personajes.elegirAtacado(jugador1) - 1], personaje);
							break;
						case 3:
							if (personaje instanceof Medico) {
								personaje.usarHabilidad(jugador1, jugador2, personaje);
							} else if (personaje instanceof Mago) {
								personaje.usarHabilidad(jugador1, jugador2, personaje);
							} else {
								personaje.usarHabilidad();
							}
						}
					} else {
						System.out
								.println(personaje.getNombrePersonaje() + ANSI_WHITE + ", no tiene vida" + ANSI_RESET);
					}
				}
			}
		} while (!haPerdido);
	
	}
	//METODO QUE INCREMENTA LAS VICTORIAS
	private void incrementarVictoriasJugador(Usuario usuario) {
		usuario.setNumeroVictoriasUsuario(usuario.getNumeroVictoriasUsuario() + 1);

		try {
			UsuarioDAO.actualizarVictoriasUsuario(usuario, conexion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//METODO QUE SUBE NIVEL A UN USUARIO
	private void subirNivel(Usuario usuario) {
		if (!usuario.equals(IA)) {
			if (usuario.getNivel() < 50) {
				System.out.println("");
				System.out.println(ANSI_PURPLE + "HAS SUBIDO DE NIVEL +2 A TUS ESTADISTICAS" + ANSI_RESET);
				System.out.println("");
				usuario.setNivel(usuario.getNivel() + 2);
			} else if (usuario.getNivel() >= 50) {
				System.out.println(" ");
				System.out.println(ANSI_YELLOW + "Ya estás en el nivel máximo" + ANSI_RESET);

			}
		}
	}
	//AÑADE UNA PARTIDA AL HISTORIAL
	public static void añadirAlHistorial(int idUsuario1, int idUsuario2, String faccionGanadora, int idUsuarioGanador, Connection conexion) {
	    try {
	        HistorialDAO.guardarHistorial( Juego.idPartida, idUsuarioGanador, faccionGanadora, conexion);
	        System.out.println("El resultado de la partida se ha añadido al historial.");
	    } catch (SQLException e) {
	        System.out.println("Error al añadir el resultado de la partida al historial.");
	        e.printStackTrace();
	    }
	}
	
}