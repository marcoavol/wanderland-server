package com.wanderland.wanderlandserver.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Read/write jpg file of photo from/to file system
 *
 * @author Marco Volken
 * @author Irene Keller

 */


@Repository
public class FileSystemRepository {

    /**
     * @param content  byte array of image
     * @param fileName  name of jpg file
     * @param location  path to jpg file

     * @throws IOException
     */


    public Path saveInFileSystem(byte[] content, String fileName, String location) throws IOException {
        Path filePath = Path.of( location + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
        return filePath;
    }


    /**
     *
     * @param path  path to jpg file
     * @return FileSystemResource
     */

    public Optional<FileSystemResource> findInFileSystem(Path path) {
        FileSystemResource resource = null;
        try {
            resource = new FileSystemResource(path);
        } catch (IllegalArgumentException e) { }
        return Optional.ofNullable(resource);
    }

}
