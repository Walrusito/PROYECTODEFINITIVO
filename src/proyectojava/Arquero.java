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

public class Arquero extends Personajes {

    private String nombreArquero;
    private static final int VIDA_INICIAL = 40;
    private static final int ESTAMINA_INICIAL = 40;
    private static final int FUERZA_INICIAL = 10;
    private static final int MANA_INICIAL = 0;
    private int vidaArquero;
    private int estaminaArquero;
    private int fuerzaArquero;
    private int manaArquero;
    private boolean habilidadActiva = false;
    private final String tipo = "arquero";

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

    public Arquero(String nombrePersonaje) {
        super(nombrePersonaje, VIDA_INICIAL, ESTAMINA_INICIAL, FUERZA_INICIAL, MANA_INICIAL);
        this.vidaArquero = VIDA_INICIAL;
        this.estaminaArquero = ESTAMINA_INICIAL;
        this.fuerzaArquero = FUERZA_INICIAL;
        this.manaArquero = MANA_INICIAL;
        this.nombreArquero = nombrePersonaje;
    }

    public Arquero(String nombrePersonaje, int vida, int estamina, int fuerza, int mana) {
        super(nombrePersonaje, vida, estamina, fuerza, mana);
        this.vidaArquero = vida;
        this.estaminaArquero = estamina;
        this.fuerzaArquero = fuerza;
        this.manaArquero = mana;
        this.nombreArquero = nombrePersonaje;
    }

    @Override
    public void usarHabilidad() {
        System.out.println(nombreArquero + ANSI_YELLOW + " HA ACTIVADO EL MODO LEGOLAS (PRECISION 100%)" + ANSI_RESET);
        habilidadActiva = true;
    }

    public boolean isHabilidadActiva() {
        return habilidadActiva;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    protected void restablecerEstadisticasPersonaje(Personajes  personaje) {
        this.vidaArquero = VIDA_INICIAL;
        this.estaminaArquero = ESTAMINA_INICIAL;
        this.fuerzaArquero = FUERZA_INICIAL;
        this.manaArquero = MANA_INICIAL;
        this.habilidadActiva=false;
    }
}