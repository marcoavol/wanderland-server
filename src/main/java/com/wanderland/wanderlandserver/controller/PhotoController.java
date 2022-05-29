package com.wanderland.wanderlandserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wanderland.wanderlandserver.entities.Photo;
import com.wanderland.wanderlandserver.entities.Route;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


import com.wanderland.wanderlandserver.services.PhotoDTO;


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
        PhotoDTO details = gson.fromJson(photoDetails, PhotoDTO.class);
//        System.out.println(gson.toJson(details));

        // Write photo to file
        String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
        String dirPath = "src/main/resources/photos/";
        Path newFile = Paths.get(dirPath + fileName);
        Files.write(newFile, photo.getBytes());

        // Photo Objekt instanziieren (noch mit leeren routes)
        Photo current_photo = new Photo();
        current_photo.setLon(details.getLon());
        current_photo.setLat(details.getLat());
        current_photo.setCaptureIsoDate(details.getCaptureIsoDate());
        current_photo.setSrc(newFile.toString());
        // Sanity check:
       // System.out.println(current_photo.toString());

        // Kontrollieren, ob die aktuelle Route-ID in der db schon existiert
        // Wenn ja: Das aktuelle Photo zur existierenden Route hinzufügen
        // Wenn nein: ein neues Route Objekt instanziieren und das aktuelle Photo hinzufügen
        for (Long id : details.getRouteIds()){
            // Alternativ könnte man auch alle Ids gleichzeitig abfragen und eine Liste aller übereinstimmenden Routen zurückerhalten
            // Vorteil wäre dass man weniger Db Abfragen machen müsste. Das ist aber wahrscheinlich im Moment nicht so ein Problem.
            // Ich finde das Aktualisieren der Db intuitiver, so wie es im Moment ist
            Optional<Route> current_route = routeRepository.findById(id);
            if(current_route.isPresent()){
                // Sanity check
                System.out.println("Route existiert schon");
                Route current_route_obj = current_route.get();

                // Sanity check
                System.out.println("Schon vorhandene Fotos: ");
                for(Photo p : current_route_obj.getPhotos()){
                    System.out.println(p.toString());
                }

                current_route_obj.addPhoto(current_photo);

                // Sanity check
                System.out.println("Fotos nach dem Hinzufügen des aktuellen Fotos: ");
                for(Photo p : current_route_obj.getPhotos()){
                    System.out.println(p.toString());
                }

                routeRepository.save(current_route_obj);
            } else
            {
                // Sanity check
                System.out.println("Route neu");
                Route current_route_obj = new Route();
                current_route_obj.setRoute_id(id);
                current_route_obj.addPhoto(current_photo);
                routeRepository.save(current_route_obj);
            }
        }

    // To do: routes in current_photo ergänzen und current_photo in db speichern

        return ResponseEntity.created(URI.create("http://localhost:8080/photos/1234")).build();


    }

}


