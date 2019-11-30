package src;

import src.presentación.Menu;;

/**
 * Esta clase representa el inicio de ejecución de nuestro programa.
 * 
 * @author Pau Val
 */

public class Main {
    /**
     * Función que llama por defecto Java al iniciar la ejecución.
     * 
     * @param args representa los parametros de entrada. No se espera ninguno.
     */
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.start();
    }
}

