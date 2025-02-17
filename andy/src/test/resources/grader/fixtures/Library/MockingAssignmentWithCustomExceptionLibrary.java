package delft;

class MyService {
    private final AnotherService anotherService;

    public MyService(AnotherService anotherService) {
        this.anotherService = anotherService;
    }

    public boolean myMethod(int a) {
        if (a == 10) {
            return false;
        }

        try {
            return anotherService.anotherMethod();
        } catch (MyException e) {
            return false;
        }
    }

}

interface AnotherService {
    boolean anotherMethod() throws MyException;
}

class MyException extends Exception {
}
