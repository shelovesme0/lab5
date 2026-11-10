import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagCounter {

    private static final Pattern TAG = Pattern.compile("<([a-zA-Z][a-zA-Z0-9-]*)");
    private static final Pattern COMMENT = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
    private static final Pattern SCRIPT_STYLE =
            Pattern.compile("(<(script|style)\\b[^>]*>).*?(</\\2\\s*>)",
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    public String download(String url) throws IOException {
        URL u = URI.create(url).toURL();
        URLConnection conn = u.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    public TreeMap<String, Integer> count(String html) {
        String cleaned = COMMENT.matcher(html).replaceAll("");
        cleaned = SCRIPT_STYLE.matcher(cleaned).replaceAll("$1$3");

        TreeMap<String, Integer> counts = new TreeMap<>();
        Matcher m = TAG.matcher(cleaned);
        while (m.find()) {
            String tag = m.group(1).toLowerCase();
            counts.merge(tag, 1, Integer::sum);
        }
        return counts;
    }

    public void printByName(Map<String, Integer> counts) {
        System.out.println("\n--- Теги в лексикографічному порядку ---");
        counts.forEach((tag, n) -> System.out.printf("%-15s %d%n", tag, n));
    }

    public void printByFrequency(Map<String, Integer> counts) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.comparingByKey()));
        System.out.println("\n--- Теги в порядку зростання частоти ---");
        for (Map.Entry<String, Integer> e : entries) {
            System.out.printf("%-15s %d%n", e.getKey(), e.getValue());
        }
    }
}
