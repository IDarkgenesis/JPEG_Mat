package src.persistencia;

import src.persistencia.File;

/**
 * Esta clase representa un archivo sin comprimir.
 * Su cometido es gestionar todas las necesidades del progama respecto este fichero.
 * Extiende la clase src.persistencia.File.
 * 
 * @author Pau Val
 */

public class UncompressedFile extends File
{
    /**
     * Constructora de un UncompressedFile.
     * 
     * @param pathName ruta al archivo sin comprimir
     */
    public UncompressedFile(String pathName)
    {
        super(pathName);
    }

    public static final long serialVersionUID = 1L;
}