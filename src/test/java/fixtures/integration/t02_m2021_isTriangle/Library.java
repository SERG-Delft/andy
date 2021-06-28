package fixtures.integration.t02_m2021_isTriangle;

class Triangle {

    public static boolean isTriangle(int a, int b, int c) {
        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
            return false;
        return true;
    }
}
