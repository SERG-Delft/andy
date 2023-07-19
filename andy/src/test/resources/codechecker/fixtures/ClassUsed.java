package delft;

import org.junit.jupiter.api.Test;

public class ClassUsed {
    /**
     * Test for statically used classes
     */
    @Test
    void t1() {
        int result = SomeStaticClass.retrieveStatically();
        assertThat(result).isEqualTo(9);
    }

    /**
     * Standard test
     */
    @Test
    void t2() {
        SomeClass someClass = new SomeClass("someField");
        String result = someClass.retrieve();
        assertThat(result).isEqualTo("someField");
    }

    /**
     * Test for checking a class that is just instantiated
     */
    @Test
    void t3() {
        SomeUselessClass us = new SomeUselessClass(9);
    }

    //Create a class outside of a method
    CreatedOutside co = new CreatedOutside(2);

    /**
     * Test for checking if a class declared outside of a test
     * is recognized as used.
     */
    @Test
    void t4() {
        co.getSomeInt();
    }

    // Example of a call to a method that is not a test
    void t5() {
        int rand = Math.random();
    }

    class SomeStaticClass{
        public static int retrieveStatically(){
            return 9;
        }
    }

    class SomeClass {
        private String someField;

        public SomeRepo(String someField) {
            this.someField = someField;
        }

        public String retrieve(){
            return this.someField;
        }
    }

    class SomeUselessClass {
        int uselessInt;
        public SomeUselessClass(int uselessInt){
            this.uselessInt=uselessInt;
        }

        int getUselessInt(){
            return this.uselessInt;
        }
    }
    }


