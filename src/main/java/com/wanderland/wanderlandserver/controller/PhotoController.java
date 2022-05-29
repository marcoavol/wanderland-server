package com.wanderland.wanderlandserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wanderland.wanderlandserver.entities.Photo;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
public class PhotoController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    RouteRepository routeRepository;


    @PostMapping(path = "/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> savePhoto(@RequestPart MultipartFile photo, @RequestParam String photoDetails) throws IOException {
//        System.out.println(photo.getContentType());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PhotoDetails details = gson.fromJson(photoDetails, PhotoDetails.class);
//        System.out.println(gson.toJson(details));


        // Write photo to file
        String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
        Path newFile = Paths.get("src/main/resources/images/" + fileName);
        Files.write(newFile, photo.getBytes());






        return ResponseEntity.created(URI.create("http://localhost:8080/photos/1234")).build();


    }

}

class PhotoDetails {
    private String captureIsoDate;
    private float lon;
    private float lat;
    private int[] routeIds;
}
