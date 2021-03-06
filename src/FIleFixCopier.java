import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FIleFixCopier extends SimpleFileVisitor<Path> {

    Path dstPath;

    public FIleFixCopier(Path dstPath) {
        this.dstPath = dstPath.toAbsolutePath();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path dstFile = Paths.get(dstPath + "/" + file.getFileName().toString());
        Files.copy(file, dstFile, REPLACE_EXISTING);
//        System.out.println(file);
        return FileVisitResult.CONTINUE;
    }
}
