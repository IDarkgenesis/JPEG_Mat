package tests.testers;

import tests.Tester;

public class LZ78Tester {

    private Tester tester;

    public LZ78Tester()
    {
        this.tester = new Tester();
    }

    public void testVoid() 
    {
       tester.test("void","LZ78");
       tester.clean("void","LZ78");
    } 

    public void testLine()
    {
        tester.test("line","LZ78");
    }

    public void testChars()
    {
        tester.test("chars","LZ78");
    }

    public void test4kb()
    {
        tester.test("4kb","LZ78");
        tester.clean("4kb","LZ78");
    }

    public void test8kb()
    {
        tester.test("8kb","LZ78");
        tester.clean("8kb","LZ78");
    }

    public void test6mb()
    {
        tester.test("6mb","LZ78");
    }

    public void test15mb()
    {
        tester.test("15mb","LZ78");
    }

    public void testCatala()
    {
        tester.test("catala","LZ78");
    }

    public void testEspañol()
    {
        tester.test("español","LZ78");
    }

    public void testEnglish()
    {
        tester.test("english","LZ78");
    }
}