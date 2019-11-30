package src.dominio.algoritmos;

import java.util.*;
import src.persistencia.*;
import src.dominio.ByteArrayHelper;

/**
 * Esta clase representa el algoritmo de compresión y descompresión LZ78.
 * Se encarga de comprimir y descomprimir archivos. Usando parejas de enteros y caracteres donde los primeros referencian a los segundos.
 * 
 * @author Pol Aguilar
 */

public class LZ78 extends Algorithm{

     /**
     * Comprime todo el texto del archivo representado por uncompressed.
     * Se va leyendo y tratando cada caracter de uno en uno.
     * 
     * @param uncompressed archivo a comprimir
     * @return array de bytes con el texto comprimido (no es legible, hay que descomprimirlo)
     * 
     * @see src.persistencia.UncompressedFile
     */
    public byte[] compress(UncompressedFile uncompressed){
        byte[] result = new byte[0];
        HashMap<String, Integer> codeWord = new HashMap<String, Integer>();
        String current ="";
        int key = 0;
        char c;
        boolean found = false;
        while((c = uncompressed.readChar()) != 0){
            current += c;
            found = false;
            if(codeWord.containsKey(current)){
                key = codeWord.get(current);
                found = true;
            }
            else {
                result = ByteArrayHelper.concatenate(result , ByteArrayHelper.intToByteArray(key,3));
                byte[] aux = {(byte) c};
                result = ByteArrayHelper.concatenate(result, aux);
               // System.out.print(c + " ");
                codeWord.put(current,codeWord.size()+1);
                key = 0;
                current = "";
            }
        }
         if(found){ //!current.equals("")
              result = ByteArrayHelper.concatenate(result ,ByteArrayHelper.intToByteArray(0,3));
              byte[] aux = {(byte)current.charAt(0)};
              result = ByteArrayHelper.concatenate(result,aux);
            //  System.out.print("\ny" + (byte) current.charAt(0) + "\nx");
         } 

        return result;
    }

  /**
     * Descomprime el archivo representado por compressed.
     * Lee enteros y caracteres y los trata con un dictionary para descomprimirlo.
     * 
     * @param compressed archivo a descomprimir
     * @return array de bytes con el texto descomprimido
     * 
     * @see src.persistencia.CompressedFile
     */

    public byte[] decompress(CompressedFile compressed){
        ArrayList<String> dictionary = new ArrayList<String>();
        int first;
        char last;
        byte[] pair;
        while((pair = compressed.readContent(4)).length != 0) {
            byte[] convert = Arrays.copyOfRange(pair,0,2);
            first = ByteArrayHelper.byteArrayToInt(convert);
            last = (char) pair[3];
            if(first == 0){
                dictionary.add(last+"");
            }
            else{
                dictionary.add(dictionary.get(first-1)+last);
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String d : dictionary){
            sb.append(d);
         //   System.out.print(d + " ");
        }
        return sb.toString().getBytes();
    }

     /**
     * Retorna el nombre de este algoritmo
     * 
     * @return nombre del algoritmo
     */

    public String getName()
    {
        return "LZ78";
    }
}