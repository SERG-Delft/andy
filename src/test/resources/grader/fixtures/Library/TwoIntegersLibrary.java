package tudelft.domain;

class TwoIntegers {

    public int sum(int n, int m) {
        if (n < 1 || n > 99 || m < 1 || m > 99) {
            throw new IllegalArgumentException();
        }
        return n + m;
    }
}
