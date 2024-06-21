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


public class Medico extends Personajes {

    private String nombreMedico;
    private static final int VIDA_INICIAL = 50;
    private static final int ESTAMINA_INICIAL = 13;
    private static final int FUERZA_INICIAL = 15;
    private static final int MANA_INICIAL = 30;
    private int vidaMedico;
    private int estaminaMedico;
    private int fuerzaMedico;
    private int manaMedico;
    private int fuerzaMagica = 0;
    private final String tipo;

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

    public Medico(String nombrePersonaje) {
        super(nombrePersonaje, VIDA_INICIAL, ESTAMINA_INICIAL, FUERZA_INICIAL, MANA_INICIAL);
        this.vidaMedico = VIDA_INICIAL;
        this.estaminaMedico = ESTAMINA_INICIAL;
        this.fuerzaMedico = FUERZA_INICIAL;
        this.manaMedico = MANA_INICIAL;
        this.nombreMedico = nombrePersonaje;
        this.tipo = "medico";
    }

    public Medico(String nombrePersonaje, int vida, int estamina, int fuerza, int mana, int fuerzaMagica) {
        super(nombrePersonaje, vida, estamina, fuerza, mana);
        this.vidaMedico = vida;
        this.estaminaMedico = estamina;
        this.fuerzaMedico = fuerza;
        this.manaMedico = mana;
        this.nombreMedico = nombrePersonaje;
        this.fuerzaMagica = fuerzaMagica;
        this.tipo = "medico";
    }

    @Override
    public Personajes[] usarHabilidad(Personajes[] jugador, Personajes[] jugador2, Personajes medico) {
        boolean haElegido = false;
        int curacionArea = 5;
        int curacionIndividual = 10;
        int costeMana = 10;
        System.out.println(ANSI_GREEN + "Señale que habilidad desea realizar (costo 10 maná) :" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "1.- Curacion en area( 5 de curacion ), 2.- Curacion divina (10 de curacion)"
                + ANSI_RESET);

        do {
            Scanner sc = new Scanner(System.in);
            try {
                int opcion = sc.nextInt();
                switch (opcion) {
                    case 1:
                        for (Personajes personaje : jugador) {
                            personaje.setVida(personaje.getVida() + curacionArea + fuerzaMagica);
                            System.out.println(personaje.getNombrePersonaje() + " " + personaje.getVida() + ANSI_GREEN
                                    + " HP " + ANSI_RESET);
                        }
                        medico.setMana(medico.getMana() - costeMana);
                        System.out.println("Personajes curados");
                        haElegido = true;
                        break;
                    case 2:
                        int aux = Personajes.elegirAtacado(jugador) - 1;
                        jugador[aux].setVida(jugador2[aux].getVida() + curacionIndividual + fuerzaMagica);
                        medico.setMana(medico.getMana() - costeMana);
                        System.out.println(jugador[aux].getNombrePersonaje() + " " + jugador[aux].getNombrePersonaje()
                                + ANSI_GREEN + " HP " + ANSI_RESET);
                        System.out.println("Personaje curado");

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
        this.vidaMedico = VIDA_INICIAL;
        this.estaminaMedico = ESTAMINA_INICIAL;
        this.fuerzaMedico = FUERZA_INICIAL;
        this.manaMedico = MANA_INICIAL;
        this.fuerzaMagica = 0;
    }
}