package src.dominio;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import java.text.SimpleDateFormat;
import java.util.Date;

import src.dominio.algoritmos.*;

/**
 * Esta clase representa un actor que hara funciones tanto de compresión como descompresión.
 * Contiene funciones necesarias para obtener estadísticas o escribir en los archivos de destino.
 * 
 * @author Pau Val
 */

public class Actor
{
    /** Inicio de la ejecución */
    protected long startTime;

    /** Fin de la ejecución */
    protected long stopTime;

    /**
     * Archivo de destino
     * 
     * @see java.io.File
     */
    protected File destinationFile;

    /** Algotirmo con el que se realizará la acción */
    protected Algorithm algorithm;

    /**
     * Construye un Actor.
     * 
     * @param destinationFile archivo de destino
     * @param algorithmName nombre del algoritmo a usar
     * 
     * @see java.io.File
     * @see src.dominio.Actor::setAlgortihm()
     */
    public Actor(File destinationFile, String algorithmName)
    {
        this.destinationFile = destinationFile;
        this.algorithm = setAlgorithm(algorithmName);
    }

    /**
     * Guarda el inicio de una ejecución
     */
    protected void initStadistics()
    {
        startTime = System.currentTimeMillis();
    }

    /**
     * Guarda el tiempo del fin de la ejecución y escribe por pantalla en un formato legible el tiempo de ejecución.
     * 
     * @see java.text.SimpleDateFormat
     * @see java.util.Date
     */
    protected void printStadistics()
    {
        stopTime = System.currentTimeMillis();
        System.out.print("Done in " + (new SimpleDateFormat("mm 'minute(s)' ss 'second(s)' SSS 'milliseconds'")).format(new Date(stopTime - startTime)) + ".\n");
    }

    /**
     * Escribe en el fichero de destino un array de bytes. Los bytes se escriben al final del archivo.
     * Si el archivo no existe lo crea.
     * 
     * @param bytes array de bytes a escribir
     * 
     * @see java.nio.file.Files
     * @see java.nio.file.StandardOpenOption
     */
    protected void writeInDestiantionFile(byte[] bytes)
    {   
        try{
            Files.write(destinationFile.toPath(), bytes, StandardOpenOption.APPEND, StandardOpenOption.CREATE);}
        catch (IOException e){
            System.out.print("Error writing in destination file.");
        }
    }

    /**
     * Del nombre de un algoritmo se retorna una instancia de la clase del algoritmo.
     * 
     * @param algorithmName nombre del algoritmo
     * @return instancia del algoritmo
     * 
     * @see src.dominio.algoritmos.Algorithm
     */
    protected Algorithm setAlgorithm(String algorithmName)
    {
        if(algorithmName.equals("LZ78")) return new LZ78();
        else if(algorithmName.equals("LZSS")) return new LZSS();
        else if(algorithmName.equals("LZW")) return new LZW();
        else if(algorithmName.equals("JPEG")) return new JPEG();
        return new LZW();
    }
}