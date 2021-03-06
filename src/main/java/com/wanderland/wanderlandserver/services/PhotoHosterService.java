package com.wanderland.wanderlandserver.services;

import com.wanderland.wanderlandserver.repositories.FileSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;

/**
 * A service that provides file hosting capabilities by using local filesystem.
 *
 * @author Marco Volken
 */

@Service
public class PhotoHosterService implements FileHoster {

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Value("${app.base-url}")
    private String BASE_URL;

    @Value("${app.public-resources-path}")
    private String PUBLIC_RESOURCES_PATH;

    private final String PHOTO_UPLOAD_PATH = "src/main/upload/photos";

    @Override
    public URL hostFile(byte[] fileContent, String fileName) throws Exception {
        String uniqueFileName = new Date().getTime() + "-" + fileName;
        this.fileSystemRepository.saveInFileSystem(fileContent, uniqueFileName, PHOTO_UPLOAD_PATH);
        return new URL(BASE_URL + PUBLIC_RESOURCES_PATH + "/photos/" + uniqueFileName);
    }

    /**
     * A convenience method to retrieve a resource in local filesystem by its unique filename.
     * @see FileSystemRepository#findInFileSystem(Path) 
     */
    public Optional<FileSystemResource> load(String fileName) {
        Path filePath = Path.of(PHOTO_UPLOAD_PATH + "/" + fileName);
        return this.fileSystemRepository.findInFileSystem(filePath);
    }

}
