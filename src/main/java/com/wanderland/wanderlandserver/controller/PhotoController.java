package com.wanderland.wanderlandserver.controller;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import com.wanderland.wanderlandserver.domain.dto.PhotoDTO;
import com.wanderland.wanderlandserver.domain.PhotoInfo;
import com.wanderland.wanderlandserver.services.PhotoHosterService;
import com.wanderland.wanderlandserver.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class PhotoController {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoService photoService;

    @Autowired
    PhotoHosterService photoHosterService;

    @PostMapping(path = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PhotoDTO> createPhoto(@RequestPart MultipartFile photoFile, @RequestPart PhotoInfo photoInfo) {
        Set<Route> routes = this.routeRepository.createOrGet(photoInfo.getRouteIds());
        // TODO: Add better (more specific) exeption handling instead of passing all possible exceptions up to here
        Photo photo;
        try {
            URL photoURL = this.photoService.saveFile(photoFile);
            photo = this.photoService.toPhoto(photoURL.toString(), photoInfo, routes);
            photoRepository.save(photo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity(this.photoService.toDTO(photo), HttpStatus.CREATED);
    }

    @GetMapping(path = "/photos/{routeId}")
    @CrossOrigin(origins = {"http://localhost:4200"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PhotoDTO[]> getPhotosByRouteId(@PathVariable Integer routeId) {
        Optional<Route> route = this.routeRepository.findById(routeId);
        Set<Photo> photos = route.isPresent() ? route.get().getPhotos() : new HashSet<Photo>();
        PhotoDTO[] photoDTOs = photos.stream().map(this.photoService::toDTO).toArray(PhotoDTO[]::new);
        return new ResponseEntity(photoDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "/resources/photos/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @CrossOrigin(origins = {"http://localhost:4200"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FileSystemResource> getPhotoByFileName(@PathVariable String fileName) {
        // TODO: Add better (more specific) exeption handling instead of passing all possible exceptions up to here
        FileSystemResource photoResource;
        try {
            photoResource = this.photoHosterService.load(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity(photoResource, HttpStatus.OK);
    }

}


