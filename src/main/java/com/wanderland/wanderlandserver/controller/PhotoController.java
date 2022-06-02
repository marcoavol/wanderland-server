package com.wanderland.wanderlandserver.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wanderland.wanderlandserver.entities.Photo;
import com.wanderland.wanderlandserver.entities.Route;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import com.wanderland.wanderlandserver.services.GetPhotoDTO;
import com.wanderland.wanderlandserver.services.PhotoInfo;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
public class PhotoController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    RouteRepository routeRepository;

    @GetMapping(path = "/photos/{routeId}")
    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getPhotosByRouteId(@PathVariable Long routeId) {
        Optional<Route> route = routeRepository.findById(routeId);
        Set<Photo> photos_on_route = route.isPresent() ? route.get().getPhotos() : new HashSet<Photo>();

        // Photo zu GetPhotoDTO konvertieren. Insbesondere wird routes vereinfacht zu einem Array nur mit den IDs.
        // Das ist zwingend nötig, sonst kommt es im nächsten Schritt zu einem StackOverflowError
        Set<GetPhotoDTO> photos_dto = new HashSet<>();
        for(Photo p : photos_on_route){
            photos_dto.add(p.convertToPhotoDTO(p));
        }

        Gson objGson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = objGson.toJson(photos_dto);

        return ResponseEntity.ok(jsonString);

    }


    @PostMapping(path = "/photos", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @CrossOrigin(origins = "http://localhost:4200")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> savePhoto(@RequestPart MultipartFile photo, @RequestParam String photoDetails) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PhotoInfo details = gson.fromJson(photoDetails, PhotoInfo.class);

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

         // Kontrollieren, ob die aktuelle Route-ID in der db schon existiert
        // Wenn ja: Das aktuelle Photo zur existierenden Route hinzufügen
        // Wenn nein: ein neues Route Objekt instanziieren und das aktuelle Photo hinzufügen
        for (Long id : details.getRouteIds()){
            // Alternativ könnte man auch alle Ids gleichzeitig abfragen und eine Liste aller übereinstimmenden Routen zurückerhalten
            // Vorteil wäre dass man weniger Db Abfragen machen müsste. Das ist aber wahrscheinlich im Moment nicht so ein Problem.
            // Ich finde das Aktualisieren der Db intuitiver, so wie es im Moment ist
            Optional<Route> current_route = routeRepository.findById(id);
            if(current_route.isPresent()){
                Route current_route_obj = current_route.get();
                current_route_obj.addPhoto(current_photo);
                current_photo.addRoute(current_route_obj);
                routeRepository.save(current_route_obj); // Wenn ich das hier weglasse und dafür in Photo cascade = CascadeType.PERSIST hinzufüge, kommt es beim speichern des Photos (Zeile 93) zu folgendem Fehler:
                // 2022-06-02 11:56:51.890 ERROR 15872 --- [nio-8080-exec-2] o.h.engine.jdbc.spi.SqlExceptionHelper   : Eindeutiger Index oder Primärschlüssel verletzt: "PRIMARY KEY ON PUBLIC.ROUTE(ROUTE_ID) ( /* key:283 */ CAST(283 AS BIGINT))"
                //Unique index or primary key violation: "PRIMARY KEY ON PUBLIC.ROUTE(ROUTE_ID) ( /* key:283 */ CAST(283 AS BIGINT))"; SQL statement:
                //insert into route (route_id) values (?) [23505-212]
            } else
            {
                Route current_route_obj = new Route();
                current_route_obj.setRoute_id(id);
                current_route_obj.addPhoto(current_photo);
                current_photo.addRoute(current_route_obj);
                routeRepository.save(current_route_obj);
            }
        }

        photoRepository.save(current_photo);

        return ResponseEntity.created(URI.create("http://localhost:8080/photos/1234")).build();

    }





}


