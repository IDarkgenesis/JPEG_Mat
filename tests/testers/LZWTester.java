package tests.testers;

import tests.Tester;

public class LZWTester {

    private Tester tester;

    public LZWTester()
    {
        this.tester = new Tester();
    }

    public void testVoid() 
    {
       tester.test("void","LZW");
    } 

    public void testLine()
    {
        tester.test("line","LZW");
    }

    public void testChars()
    {
        tester.test("chars","LZW");
    }

    public void test4kb()
    {
        tester.test("4kb","LZW");
    }

    public void test8kb()
    {
        tester.test("8kb","LZW");
    }

    public void test6mb()
    {
        tester.test("6mb","LZW");
    }

    public void test15mb()
    {
        tester.test("15mb","LZW");
    }

    public void testCatala()
    {
        tester.test("catala","LZW");
    }

    public void testEspañol()
    {
        tester.test("español","LZW");
    }

    public void testEnglish()
    {
        tester.test("english","LZW");
    }
}