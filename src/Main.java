import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    private static final String DEFAULT_URL = "https://www.apple.com";

    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final TagCounter tagCounter = new TagCounter();
    private TreeMap<String, Integer> lastTagCounts;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        while (true) {
            System.out.println("\n===== Task 5: I/O Streams =====");
            System.out.println("1. Рядок з максимальною кількістю слів у файлі");
            System.out.println("2. Зашифрувати файл");
            System.out.println("3. Розшифрувати файл");
            System.out.println("4. Підрахувати теги на сторінці (URL)");
            System.out.println("5. Зберегти результати підрахунку тегів у файл");
            System.out.println("6. Завантажити результати підрахунку тегів з файлу");
            System.out.println("0. Вихід");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> longestLine();
                    case "2" -> encrypt();
                    case "3" -> decrypt();
                    case "4" -> countTags();
                    case "5" -> saveTags();
                    case "6" -> loadTags();
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Невідома команда, спробуйте ще раз.");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Файл не знайдено або немає доступу: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Помилка вводу/виводу: " + e.getMessage());
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Файл має неправильний формат: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректні дані: " + e.getMessage());
            }
        }
    }

    // Завдання 1
    private void longestLine() throws IOException {
        String path = askPath("Введіть шлях до текстового файлу: ");
        List<String> lines = fileManager.readLines(path);
        int index = TextAnalyzer.findLineWithMostWords(lines);
        if (index == -1) {
            System.out.println("Файл порожній.");
            return;
        }
        String line = lines.get(index);
        System.out.printf("Рядок №%d (%d слів):%n%s%n",
                index + 1, TextAnalyzer.countWords(line), line);
    }

    // Завдання 3
    private void encrypt() throws IOException {
        String in = askPath("Файл для шифрування: ");
        String out = askPath("Куди зберегти зашифрований файл: ");
        char key = askKey();
        fileManager.encryptFile(in, out, key);
        System.out.println("Файл зашифровано -> " + out);
    }

    private void decrypt() throws IOException {
        String in = askPath("Зашифрований файл: ");
        String out = askPath("Куди зберегти розшифрований файл: ");
        char key = askKey();
        fileManager.decryptFile(in, out, key);
        System.out.println("Файл розшифровано -> " + out);
    }

    // Завдання 4
    private void countTags() throws IOException {
        System.out.print("Введіть URL (Enter = " + DEFAULT_URL + "): ");
        String url = scanner.nextLine().trim();
        if (url.isEmpty()) {
            url = DEFAULT_URL;
        }
        if (!url.matches("(?i)^https?://.+")) {
            throw new IllegalArgumentException("URL має починатися з http:// або https://");
        }
        System.out.println("Завантаження " + url + " ...");
        String html = tagCounter.download(url);
        lastTagCounts = tagCounter.count(html);
        System.out.println("Знайдено різних тегів: " + lastTagCounts.size());
        tagCounter.printByName(lastTagCounts);
        tagCounter.printByFrequency(lastTagCounts);
    }

    private void saveTags() throws IOException {
        if (lastTagCounts == null) {
            System.out.println("Спочатку виконайте пункт 4.");
            return;
        }
        String path = askPath("Шлях та ім'я файлу для збереження: ");
        fileManager.saveObject(lastTagCounts, path);
        System.out.println("Збережено -> " + path);
    }

    @SuppressWarnings("unchecked")
    private void loadTags() throws IOException, ClassNotFoundException {
        String path = askPath("Шлях до файлу з результатами: ");
        lastTagCounts = (TreeMap<String, Integer>) fileManager.loadObject(path);
        tagCounter.printByName(lastTagCounts);
        tagCounter.printByFrequency(lastTagCounts);
    }

    private String askPath(String prompt) {
        System.out.print(prompt);
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            throw new IllegalArgumentException("шлях не може бути порожнім");
        }
        return path;
    }

    private char askKey() {
        System.out.print("Ключовий символ (один символ): ");
        String key = scanner.nextLine();
        if (key.length() != 1) {
            throw new IllegalArgumentException("ключ має бути рівно одним символом");
        }
        return key.charAt(0);
    }
}
