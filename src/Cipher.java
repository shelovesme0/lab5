public final class Cipher {

    private Cipher() {
    }

    public static char encrypt(char c, char key) {
        return (char) (c + key);
    }

    public static char decrypt(char c, char key) {
        return (char) (c - key);
    }
}
