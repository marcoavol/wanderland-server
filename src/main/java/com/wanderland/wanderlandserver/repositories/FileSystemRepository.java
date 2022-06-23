package com.wanderland.wanderlandserver.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Handles persistence of resources in local filesystem.
 *
 * @author Marco Volken
 * @author Irene Keller
 */

@Repository
public class FileSystemRepository {

    /**
     * Writes given content into a file with given name at a given location in the local filesystem.
     * If directory for given location does not yet exist, it will be created.
     * @param content the content of the file as a bytearray
     * @param fileName the name under which the file shall be saved
     * @param location the location in the local filesystem where the file shall be saved
     * @return the path to the file into which given content has been written
     * @throws IOException
     */
    public Path saveInFileSystem(byte[] content, String fileName, String location) throws IOException {
        Path filePath = Path.of( location + "/" + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content);
        return filePath;
    }

    /**
     * Tries to retrieve the resource at given path in local filesystem.
     * @param path the path in local filesystem where requested resource is located
     * @return an optional containing the requested resource or null if the resource at given path couldn't be located
     */
    public Optional<FileSystemResource> findInFileSystem(Path path) {
        FileSystemResource resource = null;
        try {
            resource = new FileSystemResource(path);
        } catch (IllegalArgumentException e) { }
        return Optional.ofNullable(resource);
    }

}
