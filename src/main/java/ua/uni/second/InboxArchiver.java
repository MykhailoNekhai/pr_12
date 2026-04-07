package ua.uni.second;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class InboxArchiver {
    public static void archiveTmpFiles(Path inbox, Path archive) throws IOException {
        Objects.requireNonNull(inbox, "inbox must not be null");
        Objects.requireNonNull(archive, "archive must not be null");

        Files.createDirectories(archive);

        try (DirectoryStream<Path> tmpFiles = Files.newDirectoryStream(inbox, "*.tmp")) {
            for (Path tmpFile : tmpFiles) {
                Path target = archive.resolve(tmpFile.getFileName());
                Files.move(tmpFile, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
