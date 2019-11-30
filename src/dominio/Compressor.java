package src.dominio;

import java.io.File;

import src.dominio.Actor;
import src.persistencia.UncompressedFile;;

/**
 * Esta clase representa un actor de compresión.
 * Su cometido es llamar al algoritmo requerido y escribir el resultado en el fichero de destino.
 * 
 * @author Pau Val
 */

public class Compressor extends Actor
{
    /** 
     * Representa el fichero a comprimir
     * 
     * @see src.persistencia.UncompressedFile
     */
    private UncompressedFile uncompressedFile;

    /** Tamaño original del archivo a comprimir */
    private long originalSize;

    /**
     * Construye un Compressor.
     * 
     * @param uncompressedFile archivo a comprimir
     * @param destinationFile archivo de destino
     * @param algorithmName nombre del algoritmo a usar
     * 
     * @see java.io.File
     * @see src.persistencia.UncompressedFile
     */
    public Compressor(UncompressedFile uncompressedFile, File destinationFile, String algorithmName)
    {
        super(destinationFile,algorithmName);
        this.uncompressedFile = uncompressedFile;
    }

    /**
     * Realiza la acción de comprimir un fichero con los parametros de la constructora.
     * Se encaraga de recojer las estadísticas, escribir la cabecera del archivo comprimido y escribir el resultado.
     */
    public void compress()
    {
        initCompressStadistics();
        byte[] b = algorithm.compress(uncompressedFile);
        writeInDestiantionFile(getHeader().getBytes());
        writeInDestiantionFile(b);
        printCompressStadistics(b.length);
    }

    /**
     * Crea una cabezera con el nombre del archivo original y el nombre del algoritmo usado para la compresión.
     * @return string "atributo:valor\n"
     */
    private String getHeader()
    {
        String header = "name:"      + uncompressedFile.getName() + "\n" +
                        "algorithm:" + algorithm.getName()        + "\n" ;
        return header;
    }

    /**
     * Recoje el momento en que se inicia la compresión y el tamaño del archivo a comprimir.
     * 
     * @see src.dominio.Actor::initStadistics()
     */
    private void initCompressStadistics()
    {
        initStadistics();
        originalSize = uncompressedFile.length();
    }

    /**
     * Recoje el momento en que se acaba la compresión y el tamaño del archivo resultante.
     * 
     * @param long tamaño del archivo comprimido
     * 
     * @see src.dominio.Actor::printStadistics()
     */
    private void printCompressStadistics(long compressedSize)
    {
        printStadistics();
        System.out.print("Original size was "+originalSize+" bytes.\n");
        System.out.print("Compressed size is "+compressedSize+" bytes.\n");
        System.out.print("Compress ratio is "+((float)compressedSize/(float)originalSize)+" bytes.\n");
    }
}