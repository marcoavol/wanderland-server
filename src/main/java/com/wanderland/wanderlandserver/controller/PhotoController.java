package com.wanderland.wanderlandserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wanderland.wanderlandserver.entities.Photo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;


@RestController
public class PhotoController {

    @PostMapping(path = "/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Photo> savePhoto(@RequestPart MultipartFile photo, @RequestParam String photoDetails) {
        System.out.println(photo.getContentType());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PhotoDetails details = gson.fromJson(photoDetails, PhotoDetails.class);
        System.out.println(gson.toJson(details));
        return ResponseEntity.created(URI.create("http://localhost:8080/photos/1234")).build();
    }

}

class PhotoDetails {
    private String captureIsoDate;
    private float lon;
    private float lat;
    private int[] routeIds;
}
