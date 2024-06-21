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

public class Guerrero extends Personajes {

    private String nombreGuerrero;
    private static final int VIDA_INICIAL = 50;
    private static final int ESTAMINA_INICIAL = 25;
    private static final int FUERZA_INICIAL = 12;
    private static final int MANA_INICIAL = 0;
    private int vidaGuerrero;
    private int estaminaGuerrero;
    private int fuerzaGuerrero;
    private int manaGuerrero;
    private final String tipo = "guerrero";

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

    public Guerrero(String nombrePersonaje) {
        super(nombrePersonaje, VIDA_INICIAL, ESTAMINA_INICIAL, FUERZA_INICIAL, MANA_INICIAL);
        this.vidaGuerrero = VIDA_INICIAL;
        this.estaminaGuerrero = ESTAMINA_INICIAL;
        this.fuerzaGuerrero = FUERZA_INICIAL;
        this.manaGuerrero = MANA_INICIAL;
        this.nombreGuerrero = nombrePersonaje;
    }

    public Guerrero(String nombrePersonaje, int vida, int estamina, int fuerza, int mana) {
        super(nombrePersonaje, vida, estamina, fuerza, mana);
        this.vidaGuerrero = vida;
        this.estaminaGuerrero = estamina;
        this.fuerzaGuerrero = fuerza;
        this.manaGuerrero = mana;
        this.nombreGuerrero = nombrePersonaje;
    }

    @Override
    public void usarHabilidad() {
        if (vidaGuerrero - 10 > 0) {
            System.out.println(ANSI_RED + nombreGuerrero + " HA ACTIVADO EL MODO BERSERKER -VIDA +ATAQUE" + ANSI_RESET);
            vidaGuerrero -= 10;
            fuerzaGuerrero += 15;
        } else {
            System.out.println(ANSI_WHITE + "No puede usar esta habilidad en tus condiciones" + ANSI_WHITE);
        }
    }

    public String getTipo() {
        return tipo;
    }
    @Override
    protected void restablecerEstadisticasPersonaje(Personajes personaje) {
                personaje.setVida(Guerrero.VIDA_INICIAL);
                personaje.setEstamina(Guerrero.ESTAMINA_INICIAL);
                personaje.setFuerza(Guerrero.FUERZA_INICIAL);
                personaje.setMana(Guerrero.MANA_INICIAL);
            }
        
    


}