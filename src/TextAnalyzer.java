import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer {

    public static int countWords(String line) {
        Matcher m = Pattern.compile("[\\p{L}\\p{N}]+").matcher(line);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static int findLineWithMostWords(List<String> lines) {
        int bestIndex = -1;
        int maxWords = -1;
        for (int i = 0; i < lines.size(); i++) {
            int words = countWords(lines.get(i));
            if (words > maxWords) {
                maxWords = words;
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}
