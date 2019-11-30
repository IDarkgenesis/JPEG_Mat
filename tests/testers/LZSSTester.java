package tests.testers;

import tests.Tester;

public class LZSSTester {

    private Tester tester;

    public LZSSTester()
    {
        this.tester = new Tester();
    }

    public void testVoid() 
    {
       tester.test("void","LZSS");     
    } 

    public void testLine()
    {
        tester.test("line","LZSS");
    }

    public void testChars()
    {
        tester.test("chars","LZSS");
    }

    public void test4kb()
    {
        tester.test("4kb","LZSS");
    }

    public void test8kb()
    {
        tester.test("8kb","LZSS");
    }

    public void test6mb()
    {
        tester.test("6mb","LZSS");
    }

    public void test15mb()
    {
        tester.test("15mb","LZSS");
    }

    public void testCatala()
    {
        tester.test("catala","LZSS");
    }

    public void testEspañol()
    {
        tester.test("español","LZSS");
    }

    public void testEnglish()
    {
        tester.test("english","LZSS");
    }
}