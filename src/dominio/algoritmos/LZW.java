package src.dominio.algoritmos;

import java.util.*;
import java.lang.Math;

import src.persistencia.*;
import src.dominio.ByteArrayHelper;

/**
 * Esta clase representa el algoritmo de compresión y descompresión LZW.
 * Se encarga de comprimir y descomprimir archivos. Guarda los códigos en un número de bytes variable para augmentar la eficiencia.
 * 
 * @author Pau Val
 */
public class LZW extends Algorithm
{
    /** Número de bytes con los que se representa un código, se va actualizando en la ejecución. */
    private int nBytes = 1;

    /** Tamaño total del diccionario, se va actualizando en la ejecución. */
    private int dictSize = 128;

    /** Tamaño maximo de un entero, se va actualizando en la ejecución. */
    private int maxInt = 256;

    /**
     * Comprime todo el texto del archivo representado por uncompressed.
     * Inicialmente escribe los códigos en un byte, cuando no es suficiente hace una marca y augmenta el numero de bytes.
     * 
     * @param uncompressed archivo a comprimir
     * @return array de bytes con el texto comprimido (no es legible, hay que descomprimirlo)
     * 
     * @see src.persistencia.UncompressedFile
     */
    public byte[] compress(UncompressedFile uncompressed)
    {
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < dictSize; i++)
            dictionary.put("" + (char)i, i);

        String w = "";
        byte[] result = new byte[0];
        char c;
        while((c = uncompressed.readChar()) != 0){
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                if(dictionary.get(w) >= maxInt){ 
                    result = ByteArrayHelper.concatenate(result, ByteArrayHelper.intToByteArray(0,nBytes));
                    nBytes++;
                    maxInt = (int) Math.pow(2,nBytes * 8);
                }
                result = ByteArrayHelper.concatenate(result, ByteArrayHelper.intToByteArray(dictionary.get(w),nBytes));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        if (!w.equals(""))
            result = ByteArrayHelper.concatenate(result, ByteArrayHelper.intToByteArray(dictionary.get(w),nBytes));
        return result;
    }
    
    /**
     * Descomprime el archivo representado por compressed.
     * Inicialmente lee los códigos en un byte, cuando recive una marca augmenta el numero de bytes.
     * 
     * @param compressed archivo a descomprimir
     * @return array de bytes con el texto descomprimido
     * 
     * @see src.persistencia.CompressedFile
     */
    public byte[] decompress(CompressedFile compressed) {
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < dictSize; i++)
            dictionary.put(i, "" + (char)i);

        byte[] bc;
        String w = "" + (char) ByteArrayHelper.byteArrayToInt(compressed.readContent(nBytes));
        StringBuilder result = new StringBuilder(w);
        while((bc = compressed.readContent(nBytes)).length != 0){
            int cod = ByteArrayHelper.byteArrayToInt(bc);
            if(cod == 0) {
                nBytes++; 
                continue;
            }
            String act = "";
            if (dictionary.containsKey(cod))
                act = dictionary.get(cod);
            else if (cod == dictSize)
                act = w + w.charAt(0); 
            result.append(act);
            dictionary.put(dictSize++, w + act.charAt(0));
            w = act;
        }

        return result.toString().getBytes();
    }

    /**
     * Retorna el nombre de este algoritmo
     * 
     * @return nombre del algoritmo
     */
    public String getName()
    {
        return "LZW";
    }
}