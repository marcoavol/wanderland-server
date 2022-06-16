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

/**
 * Rest controller handling GET and POST requests from front-end
 *
 * @author Marco Volken
 * @author Irene Keller
 *
 */


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


    /**
     * Handles upload of a new photo from the front-end
     * @param photoFile  MultipartFile
     * @param photoInfo
     * @return PhotoDTO
     */

    @PostMapping(path = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<PhotoDTO> createPhoto(@RequestPart MultipartFile photoFile, @RequestPart PhotoInfo photoInfo) {
        Optional<Photo> existingPhoto = this.photoRepository.findById(this.photoService.generateId(photoInfo));
        if (existingPhoto.isPresent()) {
            return new ResponseEntity("Photo has already been uploaded.", HttpStatus.FORBIDDEN);
        }
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
        PhotoDTO photoDTO = this.photoService.toDTO(photo);
        return new ResponseEntity(photoDTO, HttpStatus.CREATED);
    }

    /**
     *
     * @param routeId  Identifier of a specific route
     * @return List of PhotoDTO objects for photos associated with the route
     */
    @GetMapping(path = "/photos/{routeId}")
    @CrossOrigin(origins = {"http://localhost:4200"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PhotoDTO[]> getPhotosByRouteId(@PathVariable Integer routeId) {
        Optional<Route> route = this.routeRepository.findById(routeId);
        Set<Photo> photos = route.isPresent() ? route.get().getPhotos() : new HashSet<Photo>();
        PhotoDTO[] photoDTOs = photos.stream().map(this.photoService::toDTO).toArray(PhotoDTO[]::new);
        return new ResponseEntity(photoDTOs, HttpStatus.OK);
    }

    /**
     *
     * @param fileName
     * @return
     */


    @GetMapping(path = "/resources/photos/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @CrossOrigin(origins = {"http://localhost:4200"})
    public ResponseEntity<FileSystemResource> getPhotoByFileName(@PathVariable String fileName) {
        Optional<FileSystemResource> photoResource = this.photoHosterService.load(fileName);
        if (!photoResource.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(photoResource.get(), HttpStatus.OK);
    }

}


