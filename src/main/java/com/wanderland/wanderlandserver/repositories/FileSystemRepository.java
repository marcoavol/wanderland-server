package com.wanderland.wanderlandserver.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Repository
public class FileSystemRepository {

    private final String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

    public Path saveInFileSystem(byte[] content, String fileName, String subdir) throws IOException {
        Path filePath = Path.of(RESOURCES_DIR + subdir + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
        return filePath;
    }

    public FileSystemResource findInFileSystem(Path path) throws NullPointerException {
        return new FileSystemResource(path);
    }

}
