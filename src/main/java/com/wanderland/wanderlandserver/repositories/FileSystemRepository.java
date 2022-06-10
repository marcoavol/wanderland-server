package com.wanderland.wanderlandserver.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Repository
public class FileSystemRepository {

    public Path saveInFileSystem(byte[] content, String fileName, String location) throws IOException {
        Path filePath = Path.of( location + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
        return filePath;
    }

    public Optional<FileSystemResource> findInFileSystem(Path path) {
        FileSystemResource resource = null;
        try {
            resource = new FileSystemResource(path);
        } catch (IllegalArgumentException e) { }
        return Optional.ofNullable(resource);
    }

}
