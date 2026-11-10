import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class EncryptWriter extends FilterWriter {

    private final char key;

    public EncryptWriter(Writer out, char key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int c) throws IOException {
        super.write(Cipher.encrypt((char) c, key));
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        char[] encrypted = new char[len];
        for (int i = 0; i < len; i++) {
            encrypted[i] = Cipher.encrypt(cbuf[off + i], key);
        }
        super.write(encrypted, 0, len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }
}
