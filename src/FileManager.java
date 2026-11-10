import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public void encryptFile(String inputPath, String outputPath, char key) throws IOException {
        try (Reader in = new InputStreamReader(new FileInputStream(inputPath), StandardCharsets.UTF_8);
             Writer out = new EncryptWriter(
                     new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8), key)) {
            copy(in, out);
        }
    }

    public void decryptFile(String inputPath, String outputPath, char key) throws IOException {
        try (Reader in = new DecryptReader(
                new InputStreamReader(new FileInputStream(inputPath), StandardCharsets.UTF_8), key);
             Writer out = new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8)) {
            copy(in, out);
        }
    }

    public void saveObject(Serializable obj, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        }
    }

    public Object loadObject(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return ois.readObject();
        }
    }

    private void copy(Reader in, Writer out) throws IOException {
        char[] buffer = new char[1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
    }
}
