package tests;

import java.io.Console;
import java.util.Arrays;

import tests.testers.*;

public class TestMenu {

    private Console console;

    public TestMenu() 
    {
        console = System.console();
    }

    public void start()
    {
        String[] possibleFunctions = {"1", "2", "3", "4", "5"};
        String operation = console.readLine("Wellcome to compressor/decompressor Tester. Please select an operation:\n"+
        " 1.All Testers\n 2.LZW Tester\n 3.LZ78 Tester\n 4.LZSS Tester\n 5.JPEG Tester\n"+
        "");
        while(!Arrays.asList(possibleFunctions).contains(operation)) 
            operation = console.readLine("Please enter a valid operation: ");
        if(operation.equals("1")) testAll();
        else if(operation.equals("2")) testLZW();
        else if(operation.equals("3")) testLZ78();
        else if(operation.equals("4")) testLZSS();
        else if(operation.equals("5")) testJPEG();
    }

    private void testLZW()
    {
        LZWTester lzwtester = new LZWTester();
        lzwtester.testVoid();
        lzwtester.testLine();
        lzwtester.testChars();
        lzwtester.test4kb();
        lzwtester.test8kb();
        lzwtester.test6mb();
        lzwtester.test15mb();
        lzwtester.testCatala();
        lzwtester.testEspañol();
        lzwtester.testEnglish();
    }

    private void testLZ78()
    {
        LZ78Tester lz78tester = new LZ78Tester();
        lz78tester.testVoid();
        lz78tester.testLine();
        lz78tester.testChars();
        lz78tester.test4kb();
        lz78tester.test8kb();
        lz78tester.test6mb();
        lz78tester.test15mb();
        lz78tester.testCatala();
        lz78tester.testEspañol();
        lz78tester.testEnglish();
    }

    private void testLZSS()
    {
        LZSSTester lzsstester = new LZSSTester();
        lzsstester.testVoid();
        lzsstester.testLine();
        lzsstester.testChars();
        lzsstester.test4kb();
        lzsstester.test8kb();
        lzsstester.test6mb();
        lzsstester.test15mb();
        lzsstester.testCatala();
        lzsstester.testEspañol();
        lzsstester.testEnglish();
    }

    private void testJPEG()
    {
        //tests del JPEG
        JPEGTester jpegtester= new JPEGTester();
        jpegtester.test1_4M();
        jpegtester.NOdiv64();
        jpegtester.test6M();
    }

    private void testAll()
    {
        testLZW();
        testLZ78();
        testLZSS();
        testJPEG();
    }

}

