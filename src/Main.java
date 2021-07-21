import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Main {

    static Path reference = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\reference");
    static Path readyFix = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\! Ready - fix");
    static Path hosts = Paths.get("C:\\Users\\Компьютер\\Desktop\\FixProcessing\\reports\\hosts.txt");


    public static void main(String[] args) throws IOException {
        HashMap<String, Path> referenceFixDirectories = getReferenceFixDirectories(reference);
        copyThree(referenceFixDirectories, readyFix, hosts);
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

    private static void copyThree(HashMap source, Path destination, Path hostsList) throws IOException {

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
}
