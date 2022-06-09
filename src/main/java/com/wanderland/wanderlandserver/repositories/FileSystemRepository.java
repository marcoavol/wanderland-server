package com.wanderland.wanderlandserver.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class FileSystemRepository {

    public Path saveInFileSystem(byte[] content, String fileName, String subdir) throws IOException, URISyntaxException {
        Path classPath = Path.of(FileSystemRepository.class.getResource("/").toURI());
        Path filePath = Path.of( classPath + subdir + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
        return filePath;
    }

    public FileSystemResource findInFileSystem(Path path) throws NullPointerException {
        return new FileSystemResource(path);
    }

}
