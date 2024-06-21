package proyectojava;

import java.util.InputMismatchException;
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

public class Mago extends Personajes {

    private String nombreMago;
    private static final int VIDA_INICIAL = 30;
    private static final int ESTAMINA_INICIAL = 10;
    private static final int FUERZA_INICIAL = 15;
    private static final int MANA_INICIAL = 50;
    private int vidaMago;
    private int estaminaMago;
    private int fuerzaMago;
    private int manaMago;
    private int fuerzaMagica = 0;
    private final String tipo = "mago";

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

    public Mago(String nombrePersonaje) {
        super(nombrePersonaje, VIDA_INICIAL, ESTAMINA_INICIAL, FUERZA_INICIAL, MANA_INICIAL);
        this.vidaMago = VIDA_INICIAL;
        this.estaminaMago = ESTAMINA_INICIAL;
        this.fuerzaMago = FUERZA_INICIAL;
        this.manaMago = MANA_INICIAL;
        this.nombreMago = nombrePersonaje;
    }

    public Mago(String nombrePersonaje, int vida, int estamina, int fuerza, int mana, int fuerzaMagica) {
        super(nombrePersonaje, vida, estamina, fuerza, mana);
        this.vidaMago = vida;
        this.estaminaMago = estamina;
        this.fuerzaMago = fuerza;
        this.manaMago = mana;
        this.nombreMago = nombrePersonaje;
        this.fuerzaMagica = fuerzaMagica;
    }

    @Override
    public Personajes[] usarHabilidad(Personajes[] jugador, Personajes[] jugador2, Personajes mago) {
        boolean haElegido = false;
        int costeMana = 10;
        int dañoRayos = 5;
        int dañoBola = 15;
        System.out.println(ANSI_PURPLE + "Señale que habilidad desea realizar :" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "1.- Rayos (4 Daño a todos), 2.- Bola de fuego (15 de daño)" + ANSI_RESET);

        do {
            Scanner sc = new Scanner(System.in);
            try {
                int opcion = sc.nextInt();
                sc.nextLine();
                switch (opcion) {
                    case 1:
                        for (Personajes personaje : jugador2) {
                            personaje.setVida(personaje.getVida() - (dañoRayos + fuerzaMagica));
                            System.out.println(personaje.getNombrePersonaje() + ANSI_GREEN + " HP : " + personaje.getVida()
                                    + ANSI_RESET);
                        }
                        mago.setMana(mago.getMana() - costeMana);
                        Personajes.mostrarJugadores(jugador2);
                        haElegido = true;
                        break;
                    case 2:
                        int aux = Personajes.elegirAtacado(jugador2) - 1;
                        jugador2[aux].setVida(jugador2[aux].getVida() - (dañoBola + fuerzaMagica));
                        mago.setMana(mago.getMana() - costeMana);
                        System.out.println(jugador2[aux].getNombrePersonaje() + ANSI_GREEN + " HP :"
                                + jugador2[aux].getVida() + ANSI_RESET);
                        haElegido = true;
                        break;

                    default:
                        System.out.println(ANSI_RED + "Opcion no válida" + ANSI_RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(ANSI_RED + "Error: Debes ingresar un número entero." + ANSI_RESET);
                sc.nextLine();
            }
        } while (!haElegido);
        return jugador;

    }

    public int getFuerzaMagica() {
        return fuerzaMagica;
    }

    public void setFuerzaMagica(int fuerzaMagica) {
        this.fuerzaMagica = fuerzaMagica;
    }

    public String getTipo() {
        return tipo;
    }

	@Override
	protected void restablecerEstadisticasPersonaje(Personajes personaje) {
			this.vidaMago = VIDA_INICIAL;
	        this.estaminaMago = ESTAMINA_INICIAL;
	        this.fuerzaMago = FUERZA_INICIAL;
	        this.manaMago = MANA_INICIAL;
	        this.fuerzaMagica = 0;
	}
}