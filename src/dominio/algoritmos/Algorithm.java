package src.dominio.algoritmos;

import src.persistencia.*;

/**
 * Esta clase representa un algoritmo de compresión y descompresión.
 * Se encarga de comprimir y descomprimir archivos.
 * 
 * @author Pau Val
 */
public abstract class Algorithm
{
    /**
     * Comprime el archivo uncompressedFile
     * 
     * @param uncompressedFile archivo a comprimir
     * @return bytes comprimidos del archivo uncompressedFile
     * 
     * @see src.persistencia.UncompressedFile
     */
    public abstract byte[] compress(UncompressedFile uncompressedFile);

    /**
     * Descomprime el archivo compressedFile
     * 
     * @param compressedFile archivo a descomprimir
     * @return bytes descomprimidos del archivo compressedFile
     * 
     * @see src.persistencia.CompressedFile
     */
    public abstract byte[] decompress(CompressedFile compressedFile);

    /**
     * Retorna el nombre del algoritmo
     * 
     * @return nombre del algoritmo
     */
    public abstract String getName();
}