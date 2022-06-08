package com.wanderland.wanderlandserver.config;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import com.wanderland.wanderlandserver.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("dev")
@Transactional
public class DevConfiguration {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    RouteRepository routeRepository;

    Photo photo1;
    Photo photo2;
    Photo photo3;
    Photo photo4;
    Photo photo5;
    Photo photo6;

    Route route1;
    Route route2;
    Route route3;

    Set<Photo> set1 = Stream.of(photo1, photo2, photo6)
            .collect(Collectors.toCollection(HashSet::new));
    Set<Photo> set2 = Stream.of(photo1, photo3)
            .collect(Collectors.toCollection(HashSet::new));
    Set<Photo> set3 = Stream.of(photo4, photo5, photo6)
            .collect(Collectors.toCollection(HashSet::new));

 //   @PostConstruct
    public void createData(){

        // Photo Objekte erstellen. Hier ist das Datenfeld "routes" noch leer
        photo1 = createPhoto((float) 46.1, (float) 8.1);
        photo2 = createPhoto((float) 46.2, (float) 8.2);
        photo3 = createPhoto((float) 46.3, (float) 8.3);
        photo4 = createPhoto((float) 46.4, (float) 8.4);
        photo5 = createPhoto((float) 46.5, (float) 8.5);
        photo6 = createPhoto((float) 46.6, (float) 8.6);

        route1 = createRoute(1, set1);
        route2 = createRoute(2, set2);
        route3 = createRoute(3, set3);

        routeRepository.save(route1);
        routeRepository.save(route2);
        routeRepository.save(route3);

        // Die Routen zu den jeweiligen Photos hinzufügen
        photo1.getRoutes().addAll(Arrays.asList(route1, route2));
        photo2.getRoutes().addAll(Arrays.asList(route1));
        photo3.getRoutes().addAll(Arrays.asList(route2));
        photo4.getRoutes().addAll(Arrays.asList(route3));
        photo5.getRoutes().addAll(Arrays.asList(route3));
        photo6.getRoutes().addAll(Arrays.asList(route1, route3));

        photoRepository.save(photo1);
        photoRepository.save(photo2);
        photoRepository.save(photo3);
        photoRepository.save(photo4);
        photoRepository.save(photo5);
        photoRepository.save(photo6);

    }

    private Photo createPhoto(float lat, float lon) {
        Photo photo = new Photo();
        photo.setLat(lat);
        photo.setLon(lon);
        photo.setSrc("");
        photo.setCaptureIsoDate("");
        return photo;
    }

     private Route createRoute(Integer id, Set<Photo> photos){
        Route route = new Route();
        route.setId(id);
        for(Photo photo : photos){
            route.addPhoto(photo);
        }
        return route;
     }

}
