package src.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Exception;

import src.persistencia.File;

/**
 * Esta clase representa un archivo comprimido.
 * Su cometido es gestionar todas las necesidades del progama respecto este fichero.
 * Extiende la clase src.persistencia.File.
 * 
 * @author Pau Val
 */

public class CompressedFile extends File
{
    /** Nombre del archivo original. */
    private String name = null;

    /** Nombre del algoritmo con el que fue comprimido. */
    private String algorithm = null;

    /**
     * Constructora de un CompressedFile.
     * Llama al lector de atributos.
     * 
     * @param pathName ruta al archivo comprimido
     * 
     * @throws Exeption lanza una excepción si el archivo no fue comprimido con este programa
     * 
     * @see src.persistencia.CompressedFile::readAttributes()
     */
    public CompressedFile(String pathName) throws Exception
    {
        super(pathName);
        readAttributes();
    }

    /**
     * Lee los atributos del archivo original almazenados en el archivo comprimido. Si esta no existe lanza una excepción.
     * 
     * @throws Exeption lanza una excepción si el archivo no fue comprimido con este programa
     *
     * @see java.io.FileInputStream::skip()
     * @see java.io.BufferedReader::readLine()
     * @see java.io.FileReader
     */
    private void readAttributes() throws Exception
    {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getPath()));
            int read = 0;
            for(int i=0; i<2; i++){
                String line = bufferedReader.readLine();
                read = read + (line+"\n").getBytes().length;
                String[] split = line.split(":");
                if(split[0].equals("name")) name = split[1];
                else if(split[0].equals("algorithm")) algorithm = split[1];
            }
            fileInputStream.skip(read);
            bufferedReader.close();
            if((name == null) || (algorithm == null)) throw new Exception("Bad compressed");
            
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna el nombre con la extensión del archivo original.
     * 
     * @return nombre con la extensión del archivo original
     */
    public String getOriginalName()
    {
        return name;
    }

    /**
     * Retorna la extensión del archivo original.
     * 
     * @return extensión del archivo original
     */
    public String getOriginalExtension()
    {
        String ext = null;
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            ext = name.substring(pos, name.length());
        }
        return ext;
    }

    /**
     * Retorna el nombre del algoritmo con el que fue comprimido.
     * 
     * @return el nombre del algoritmo con el que fue comprimido
     */
    public String getAlgorithm()
    {
        return algorithm;   
    }

    public static final long serialVersionUID = 1L;
}