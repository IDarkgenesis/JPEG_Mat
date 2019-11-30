package src.presentación;

import java.io.File;
import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

import src.dominio.Compressor;
import src.dominio.Decompressor;
import src.persistencia.*;

public class Menu
{
    private Scanner console;

    public void start()
    {
        console = new Scanner(System.in);
        //console = System.console();

        String[] possibleOperations = {"1", "2", "3"};

        //String operation = console.readLine("Wellcome to compressor/decompressor. Please select an operation:\n 1.Compress\n 2.Uncompress\n 3.Run tests\n");

        System.out.println("Wellcome to compressor/decompressor. Please select an operation: 1.Compress 2.Uncompress 3.Run tests");
        String operation=console.nextLine();

        while(!Arrays.asList(possibleOperations).contains(operation)){
            System.out.println("Please enter a valid operation: ");
            operation = console.nextLine();
        }
        if(operation.equals("1")) compress();
        else if(operation.equals("2")) decompress();
        else if(operation.equals("3")) runTest();
    }

    private void compress()
    {
        UncompressedFile uncompressedFile = readFile("Please enter the file path: ");
        File destinationFolder = readFolder("Please enter the destination folder: ");
        File destinationFile = readCompressDestinationFile(uncompressedFile, destinationFolder);
        String algorithm = readAlgorithm();

        Compressor compressor = new Compressor(uncompressedFile,destinationFile,algorithm);
        compressor.compress();
    }
    
    private void decompress()
    {
        try{
            System.out.println("Please enter the file path: ");
            String path= console.nextLine();
            CompressedFile compressedFile = new CompressedFile(path);

            File destinationFolder = readFolder("Please enter the destination folder: ");

            File destinationFile = readDecompressDestinationFile(compressedFile, destinationFolder);

            Decompressor decompressor = new Decompressor(compressedFile,destinationFile);

            decompressor.decompress();
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }

    }

    private void runTest()
    {

    }

    private String readAlgorithm()
    {
        String[] possibleAlgorithms = {"LZ78", "LZSS", "LZW", "JPEG", ""};

        System.out.println("Especify the algorithm (LZ78, LZSS, LZW, JPEG) (enter for auto): ");
        String algorithm = console.nextLine();


        while(!Arrays.asList(possibleAlgorithms).contains(algorithm)){
            System.out.println("Invalid algorithm (LZ78, LZSS, LZW, JPEG) (enter for auto): ");
            algorithm = console.nextLine();
        }

        if(algorithm.equals("")) algorithm = "auto";

        return algorithm;
    }

    private File readCompressDestinationFile(UncompressedFile file, File folder)
    {
        System.out.println("Please enter the name of the compressed file (enter for same name): ");
        String name = console.nextLine();

        if(name.equals("")) name = file.getFileName();

        File destinationFile = new File(folder.toString() + File.separator + name);

        while(destinationFile.exists()){
            System.out.println("File already exists. Please enter a valid name (enter for same name): ");
            name = console.nextLine();
            if(name.equals("")) name = file.getFileName();
            destinationFile = new File(folder.toString() + File.separator + name);
        }
        return destinationFile;
    }

    private File readDecompressDestinationFile(CompressedFile file, File folder)
    {
        File destinationFile = new File(folder.toString() + File.separator + file.getOriginalName());
        String newName = null;
        while(destinationFile.exists()){
            System.out.println("File to decompress already exists. Please insert a new name: ");
            newName = console.nextLine();
            destinationFile = new File(folder.toString() + File.separator + newName + file.getOriginalExtension());
        }
        return destinationFile;
    }

    private File readFolder(String message)
    {

        System.out.println(message);

        File folder = new File(console.nextLine());

        while(!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder not found. Please insert a valid folder: ");
            folder = new File(console.nextLine());
        }
        return folder;
    }

    private UncompressedFile readFile(String message)
    {
        System.out.println(message);
        String path = console.nextLine();
        File file = new File(path);
        while(!file.exists() || !file.isFile()){
            System.out.println("File not found. Please insert a valid file: ");
            path = console.nextLine();
            file = new File(path);
        }
        UncompressedFile uncompressedFile = new UncompressedFile(path);
        return uncompressedFile;
    }
}