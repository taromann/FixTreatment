
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.*;

public class Main {

    private static final Path reference = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\reference");
    private static final Path readyFix = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\! Ready - fix");
    private static final Path hosts = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\hosts.txt");
    private static final Path readyREPORTS = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\ReadyReports");


    public static void main(String[] args) throws IOException {
        HashMap<String, Path> referenceFixDirectories = getReferenceFixDirectories(reference);
        ArrayList<Path> filesToConvert = copyThree(referenceFixDirectories, readyFix, hosts);
        FileFixHandler(filesToConvert);



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

    private static ArrayList<Path> copyThree(HashMap source, Path destination, Path hostsList) throws IOException {

        List<String> hostList = Files.readAllLines(hostsList);
        ArrayList<Path> filesToConvert = new ArrayList<>();
        for (String host : hostList) {
            source.forEach((key, value) -> {
                try {
                    Path dstPath = Paths.get(String.format("%s\\%s (%s)\\", readyFix, host, key));
                    if (!Files.exists(dstPath)) {
                        Files.createDirectory(dstPath);
                    }
                    filesToConvert.add(Paths.get(dstPath + "/" + "Report_New.csv"));
                    Files.walkFileTree((Path) value, new FIleFixCopier(dstPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return filesToConvert;
    }

    private static void FileFixHandler(ArrayList<Path> filesToConvert) throws IOException {
        for (Path reportNewFile : filesToConvert) {
            Path readyCSV = Paths.get(String.format("%s\\%s.csv", readyREPORTS, reportNewFile.getParent().getFileName()));
            if (Files.exists(readyCSV)) {
               Files.delete(readyCSV);
            }
            Files.createFile(readyCSV);
            String text = new String(Files.readAllBytes(reportNewFile), "windows-1251");
            List<String> lines = Arrays.stream(text.split(System.lineSeparator())).toList();

            for (String line : lines) {
                String[] values = line.split(",");
//                System.out.println(Arrays.toString(values));
                System.out.println(values[1]);
                List<String> list = Arrays.asList(values);
                Files.write(readyCSV, list);
            }
        }
    }
}
