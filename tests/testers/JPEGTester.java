package tests.testers;

import tests.Tester;

public class JPEGTester {
    private Tester tester;

    public JPEGTester(){ this.tester= new Tester();}

    public void test1_4M() {
        tester.testJPEG("1_4MB","JPEG");
    }
    public void NOdiv64() {
        tester.testJPEG("NOdiv64","JPEG");
    }
    public void test6M() {
        tester.testJPEG("6M","JPEG");
    }

}