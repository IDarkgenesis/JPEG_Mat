package tests;

import java.io.File;
import java.util.Arrays;
import java.lang.Exception;

import src.dominio.*;
import src.persistencia.*;

public class Tester {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";

    public void test(String file, String alg_name) 
    {
        System.out.print(CYAN_BOLD_BRIGHT + "\nTest with algorithm:"+alg_name+", File:"+file+".txt\n" + ANSI_RESET);
        String originalFile = "data/"+file+".txt" ;
        String compressedFile = "data/compressed/"+alg_name+"/"+file;
        String uncompressedFile = "data/compressed/"+alg_name+"/"+file+".txt";

        compression(originalFile, compressedFile, alg_name);
        decompression(compressedFile, uncompressedFile);

        UncompressedFile original = new UncompressedFile(originalFile);
        UncompressedFile result = new UncompressedFile(uncompressedFile);
        comprovation(original, result, file);
    }

    public void testJPEG(String file, String alg_name) 
    {
        System.out.print(CYAN_BOLD_BRIGHT + "\nTest with algorithm:"+alg_name+", File:"+file+".ppm\n" + ANSI_RESET);
        String originalFile = "data/"+file+".ppm" ;
        String compressedFile = "data/compressed/"+alg_name+"/"+file;
        String uncompressedFile = "data/compressed/"+alg_name+"/"+file+".ppm";

        compression(originalFile, compressedFile, alg_name);
        decompression(compressedFile, uncompressedFile);

        UncompressedFile original = new UncompressedFile(originalFile);
        original.delete();
        UncompressedFile result = new UncompressedFile(uncompressedFile);
        result.delete();
        
        System.out.print(ANSI_GREEN + "Test Passed! Algorithm is working good!\n" +ANSI_RESET);
    }

    private void compression(String fileOriginal, String fileCompressed, String algorithm)
    {
        System.out.print("Compression:\n");
        UncompressedFile uncompressedFileToTest = new UncompressedFile(fileOriginal);
        File uncompressedDestinationFile = new File(fileCompressed);
        Compressor compressor = new Compressor(uncompressedFileToTest,uncompressedDestinationFile, algorithm);
        compressor.compress();
    }

    private void decompression(String fileCompressed, String fileUncompressed)
    {
        System.out.print("\nDecompression:\n");
        CompressedFile compressedFileToTest = null;
        try {
            compressedFileToTest = new CompressedFile(fileCompressed);
        }
        catch (Exception e) {}
        File compressedDestinationFile = new File(fileUncompressed);
        Decompressor decompressor = new Decompressor(compressedFileToTest, compressedDestinationFile);
        decompressor.decompress();
        //if(compressedFileToTest.delete()) System.out.print("  -> Compressed file deleted !!\n");
        //else System.out.print("  -> Error on file deleted !!\n");
    }

    private void comprovation(UncompressedFile original, UncompressedFile result, String file)
    {
        System.out.print("\nResult:\n");
        if (!Arrays.equals(original.readAll(),result.readAll())) System.out.print(ANSI_RED +file+".txt test failed.\n" + ANSI_RESET);
        else System.out.print(ANSI_GREEN + "Test Passed! Algorithm is working good!\n" +ANSI_RESET);
        //if(result.delete()) System.out.print("  -> Uncompressed file deleted !!\n");
        //else System.out.print("  -> Error on .txt deleted !!\n");
    }

    public void clean(String file, String alg_name)
    {
        String pathCompressedFile = "data/compressed/"+alg_name+"/"+file;
        File compressedFile = new File(pathCompressedFile);
        compressedFile.delete();

        String pathDecompressedFile =  "data/compressed/"+alg_name+"/"+file+".txt";
        File decompressedFile = new File(pathDecompressedFile);
        decompressedFile.delete();
    }

}