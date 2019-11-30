package src.dominio;

import java.io.File;

import src.persistencia.*;
import src.dominio.Actor;

/**
 * Esta clase representa un actor de descompresión.
 * Su cometido es llamar al algoritmo requerido y escribir el resultado en el fichero de destino.
 * 
 * @author Pau Val
 */

public class Decompressor extends Actor
{
    /** 
     * Representa el fichero a descomprimir
     * 
     * @see src.persistencia.CompressedFile
     */
    private CompressedFile compressedFile;

    /**
     * Construye un Compressor.
     * 
     * @param compressedFile archivo a descomprimir
     * @param destinationFile archivo de destino
     * 
     * @see java.io.File
     * @see src.persistencia.CompressedFile
     */
    public Decompressor(CompressedFile compressedFile, File destinationFile)
    {
        super(destinationFile, compressedFile.getAlgorithm());
        this.compressedFile = compressedFile;
    }

    /**
     * Realiza la acción de descomprimir un fichero con los parametros de la constructora.
     * Se encaraga de recojer las estadísticas y escribir el resultado.
     */
    public void decompress()
    {
        initStadistics();
        byte[] b = algorithm.decompress(compressedFile);
        writeInDestiantionFile(b);
        printStadistics();
    }
}