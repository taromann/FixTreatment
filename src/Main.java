import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final Path reference = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\reference");
    private static final Path readyFix = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\! Ready - fix");
    private static final Path hosts = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\hosts.txt");
    private static final Path readyREPORTS = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\ReadyReports");


    public static void main(String[] args) throws IOException {
        HashMap<String, Path> referenceFixDirectories = getReferenceFixDirectories(reference);
        List<Path> filesToConvert = copyThree(referenceFixDirectories, readyFix);
        FileFixHandler(filesToConvert);


    }

    private static List<Path> copyThree(Map<String, Path> referenceFixDirectories, Path readyFix) {
        List<String> hostList = Files.readAllLines(hostsList);
        for (String host : hostList) {
            source.forEach((key, value) -> {
                try {
                    Path dstPath = Paths.get(String.format("%s\\%s (%s)\\", readyFix, host, key));
                    Files.createDirectory(dstPath);
                    Files.walkFileTree((Path) value, new MyFileVisitor(dstPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static HashMap<String, Path> getReferenceFixDirectories(Path reference) throws IOException {
        DirectoryStream<Path> files = Files.newDirectoryStream(reference);
        HashMap<String, Path> references = new HashMap<>();

        for (Path file : files) {
            if (file.toString().endsWith("(K)")) {
                references.put("K", file);
            } else if (file.toString().endsWith("(S)")) {
                references.put("S", file);
            } else if (file.toString().endsWith("(D)")) {
                references.put("D", file);
            } else System.out.println("Incorrect filename");
        }
        return references;
    }


    private static void FileFixHandler(List<Path> filesToConvert) throws IOException {
        for (Path reportNewFile : filesToConvert) {
            Path readyCSV = Paths.get(String.format("%s\\%s.csv", readyREPORTS, reportNewFile.getParent().getFileName()));
//            if (Files.exists(readyCSV)) {
//                Files.delete(readyCSV);
//            }
//            Files.createFile(readyCSV);
            String text = new String(Files.readAllBytes(reportNewFile), "windows-1251");
//            String text = Files.readString(reportNewFile, Charset.defaultCharset());
            List<String> lines = Arrays.stream(text.split(System.lineSeparator())).toList();
            File file = new File(readyCSV.toString());
//            FileOutputStream out = new FileOutputStream(file, true);
//            PrintStream tempOut = System.out;
            try (FileWriter writer = new FileWriter(file, Charset.defaultCharset(), false)) {
//                PrintStream ps = new PrintStream(out);
//                System.setOut(new PrintStream(out));
                for (String line : lines) {
                    String[] values = line.split(",");
//                System.out.println(Arrays.toString(values));
//                    System.out.println(values);
//                System.out.println(readyCSV);
//                System.out.println(values.length);
                    StringBuilder sb = new StringBuilder();
                    for (String value : values) {
//                    out.write(value.getBytes());
//                    byte[] bs = value.getBytes();
                        sb.append(value).append(" ");
//                        writer.write(value);
                        System.out.println(value);
                    }
                    writer.write(sb.toString().trim());
                    System.out.println(sb);
                    writer.write("\r\n");
                }
//                out.write(values[3].getBytes());


//                FileWriter writer = new FileWriter(readyCSV.toString(), false);
//                writer.write(values[1]);


//                System.out.println(values[1]);
//                List<String> list = Arrays.asList(values);
//                Files.write(readyCSV, list);
            }
//            System.setOut(tempOut);
        }
    }
}
