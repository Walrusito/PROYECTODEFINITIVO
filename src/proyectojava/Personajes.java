package proyectojava;

import java.util.InputMismatchException;
import java.util.Random;
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

public abstract class Personajes {
    private  int idContador = 1000;
    private final int id;
    private String nombrePersonaje;
    protected int vida;
	protected int estamina;
	protected int fuerza;
	protected int mana;
    
    //COLORINES//
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Personajes(String nombrePersonaje, int vida, int estamina, int fuerza, int mana) {
        this.id = idContador++;
        this.nombrePersonaje = nombrePersonaje;
        this.vida = vida;
        this.estamina = estamina;
        this.fuerza = fuerza;
        this.mana = mana;
    }

	public String getNombrePersonaje() {
		return nombrePersonaje;
	}

	public void setNombrePersonaje(String nombrePersonaje) {
		this.nombrePersonaje = nombrePersonaje;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getEstamina() {
		return estamina;
	}

	public void setEstamina(int estamina) {
		this.estamina = estamina;
	}

	public int getFuerza() {
		return fuerza;
	}

	public void setFuerza(int fuerza) {
		this.fuerza = fuerza;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getId() {
		return id;
	}

	public boolean acierta() {
		Random random = new Random();
		boolean acierta = random.nextBoolean();
		return acierta;
	}

	public String toString() {
		return nombrePersonaje;

	}

	//METODO PARA COMPROBAR LA ESTAMINA
	public boolean comprobarEstamina(int costeEstamina, Personajes personaje) {
		return costeEstamina <= personaje.getEstamina();
	}
	//METODO PARA ELEGIR A QUIEN ATACAR
	static int elegirAtacado(Personajes[] personajes) {
	    boolean haElegido = false;
	    System.out.println(ANSI_YELLOW + "A quién quieres atacar?" + ANSI_RESET);
	    do {
	        mostrarJugadores(personajes);
	        Scanner sc = new Scanner(System.in);
	        try {
	            int opcion = sc.nextInt();
	            if (opcion >= 1 && opcion <= 3) {
	                haElegido = true;
	                return opcion;
	            } else {
	                System.out.println("Opción inválida");
	            }
	        } catch (InputMismatchException e) {
	            System.out.println(ANSI_WHITE + "Error: Debes ingresar un número entero." + ANSI_RESET);
	            sc.nextLine();
	        }
	    } while (!haElegido);
	    return 0;
	}
	//METODO PARA ELEGIR EL ATAQUE
	public int elegirAtaque(Personajes personaje) {
		Scanner sc = new Scanner(System.in);
		System.out.println(ANSI_CYAN + "ELIGE QUE ATAQUE DESEA REALIZAR : " + personaje.getNombrePersonaje()+ANSI_RESET);
		System.out.println(ANSI_CYAN+"1.- LIGERO			2.-PESADO			3.-HABILIDAD"+ANSI_RESET);
		try {
			int opcion = sc.nextInt();
			sc.nextLine();
			if (opcion >= 1 && opcion <= 3) 
			{
				return opcion;
			} else {
				System.out.println("Opción inválida. Por favor, elija una opción correcta." + ANSI_RESET);
			}
		} catch (InputMismatchException e) {
			System.out.println(ANSI_WHITE + "Error: Debes ingresar un número entero." + ANSI_RESET);
			sc.nextLine();
		}
		return 0;
	}

	//METODO PARA USAR HABILIDAD
	public Personajes[] usarHabilidad(Personajes[] jugador, Personajes[] jugador2, Personajes personaje) {
		return jugador;

	}
	//METODO PARA USAR HABILIDAD
	public void usarHabilidad() {

	}
	//METODO QUE COMPRUEBA SI QUEDAN PERSONAJES CON VIDA
	static boolean hayPersonajesConVida(Personajes[] jugadores) {
		for (Personajes personaje : jugadores) {
			if (personaje.getVida() > 0) {
				return true;
			}
		}
		return false;
	}
	//METODO QUE MUESTRA LOS PERSONAJES DE LOS JUGADORES
	public static void mostrarJugadores(Personajes[] jugador) {
		for (Personajes personaje : jugador) {
			System.out.println(ANSI_WHITE + personaje.toString() + ANSI_RESET + ANSI_GREEN + " HP " + ANSI_RESET
					+ +personaje.getVida() + ANSI_YELLOW + " ESTAMINA " + ANSI_RESET + personaje.getEstamina()
					+ ANSI_BLUE + "  MANA " + personaje.getMana()+ANSI_RESET);
		}
	}
	//METODO QUE COMPRUEBA EL MANA
	static boolean comprobarMana(Personajes personaje) {
		if (personaje.getMana() - 10 < 0) {
			return false;
		}
		return false;
	}


	//METODO QUE SUBE LA ESTAMINA CUANTO UN PERSONAJE NO TIENE MAS
	public void descansar(Personajes personaje) {
		personaje.setEstamina(personaje.getEstamina() + 10);
	}
	
	//METODO PARA HACER UN ATAQUE LIGERO
	public void ataqueLigero(Personajes personaje, Personajes atacante) {
	    int costeEstamina = 5;
	    boolean omitirAcierta = false;

	    if (atacante instanceof Arquero) {
	        omitirAcierta = ((Arquero) atacante).isHabilidadActiva();
	    }

	    if (omitirAcierta || acierta()) {
	        if (comprobarEstamina(costeEstamina, atacante)) {
	            personaje.setVida(personaje.getVida() - (atacante.getFuerza() - 10));
	            atacante.setEstamina(atacante.getEstamina() - costeEstamina);
	            System.out.println(" ");
	            System.out.println(atacante.getNombrePersonaje() + " ha usado ataque ligero a: " + personaje.getNombrePersonaje());
	            System.out.println(personaje.getNombrePersonaje() + " HP: " + personaje.getVida());
	        } else {
	            System.out.println(atacante.getNombrePersonaje() + " no tiene suficiente estamina (descansando)");
	            descansar(atacante);
	        }
	    } else {
	        System.out.println(atacante.getNombrePersonaje() + " ha fallado el ataque ligero");
	    }
	}
	//METODO PARA HACER UN ATAQUE PESADO
	public void ataquePesado(Personajes personaje, Personajes atacante) {
	    int costeEstamina = 8;
	    boolean omitirAcierta = false;

	    if (atacante instanceof Arquero) {
	        omitirAcierta = ((Arquero) atacante).isHabilidadActiva();
	    }

	    if (omitirAcierta || acierta()) {
	        if (comprobarEstamina(costeEstamina, atacante)) {
	            personaje.setVida(personaje.getVida() - (atacante.getFuerza() - 5));
	            atacante.setEstamina(atacante.getEstamina() - costeEstamina);
	            System.out.println(" ");
	            System.out.println(atacante.getNombrePersonaje() + " ha lanzado un ataque pesado a: " + personaje.getNombrePersonaje());
	            System.out.println(personaje.getNombrePersonaje() + " HP: " + personaje.getVida());
	        } else {
	            System.out.println(atacante.getNombrePersonaje() + " no tiene suficiente estamina (descansando)");
	            descansar(atacante);
	        }
	    } else {
	        System.out.println(atacante.getNombrePersonaje() + " ha fallado el ataque pesado");
	    }
	}
	protected abstract void restablecerEstadisticasPersonaje(Personajes personaje);



}
