import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.*;

public class Main {

    static Path reference = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\reference");
    static Path readyFix = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\! Ready - fix");
    static Path hosts = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\hosts.txt");
    static Path readyREPORTS = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\ReadyReports");


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
                    filesToConvert.add(Paths.get(dstPath + "/" + "Report_New.txt"));
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

            Path readyTXT = Paths.get(String.format("%s\\%s.txt", readyREPORTS, reportNewFile.getParent().getFileName()));
            if (Files.exists(readyTXT)) {
               Files.delete(readyTXT);
            }
            Files.createFile(readyTXT);
            List<String> lines = Files.readAllLines(reportNewFile);
            for (String line : lines) {
                System.out.println(line);

                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
//                    System.out.println(values[0]);
                }


            }



        }
        
    }
}
