import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Set<String> set = new TreeSet<>(Comparator.comparing(String::trim).reversed());
        HashMap<String, String> fileContents = new HashMap<>();
        String path = reader.readLine();
        getFiles(new File(path), path, set, fileContents);
        List<String> orderedList = new LinkedList<>(set);
        for (String key : set) {
            int maxIndex = 0;
            String text = fileContents.get(key);
            for (int i = 0; i < orderedList.size(); i++) {
                if (text.contains(orderedList.get(i).substring(1))) {
                    if (fileContents.get(orderedList.get(i)).contains(key)) {
                        System.out.println("Существует циклическая зависимость!");
                        orderedList.clear();
                        break;
                    }
                    maxIndex = i;
                }
            }
            if (maxIndex != 0) {
                orderedList.remove(key);
                orderedList.add(maxIndex, key);
            }
        }
        if (orderedList.size() != 0) {
            printList(orderedList);
            concatString(fileContents, orderedList);
        }
    }

    public static void getFiles(File folder, String startPath, Set<String> set, HashMap<String, String> fileContents) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFiles(file, startPath, set, fileContents);
                } else {
                    String content = Files.readString(Paths.get(file.getPath()));
                    String filename = file.getPath().replace(startPath,"").replace("\\","/");
                    set.add(filename);
                    fileContents.put(filename, content);
                }
            }
        }
    }

    public static void printList(List<String> list) {
        for (String s : list) {
            System.out.println(s);
        }
    }

    public static void concatString(HashMap<String,String> fileList, List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(fileList.get(s));
        }
        System.out.println(builder.toString());
    }
}