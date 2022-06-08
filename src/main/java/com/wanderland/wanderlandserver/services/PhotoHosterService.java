package com.wanderland.wanderlandserver.services;

import com.wanderland.wanderlandserver.repositories.FileSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.util.Date;

@Service
public class PhotoHosterService implements FileHoster {

    @Value("${app.base-url}")
    private String BASE_URL;

    @Value("${app.public-resources-path}")
    private String PUBLIC_RESOURCES_PATH;

    private final String PHOTO_RESOURCES_DIR = "/photos";

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Override
    public URL save(byte[] fileContent, String contentName) throws Exception {
        String fileName = new Date().getTime() + "-" + contentName;
        fileSystemRepository.saveInFileSystem(fileContent, fileName, PHOTO_RESOURCES_DIR);
        return new URL(BASE_URL + PUBLIC_RESOURCES_PATH + "/" + fileName);
    }

}
