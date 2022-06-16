// Based on https://springframework.guru/testing-spring-boot-restful-services/

package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class RouteRepositoryTest {
    @Autowired
    private RouteRepository routeRepo;

    private Photo photo1 = new Photo();
    private Photo photo2 = new Photo();
    Set<Photo> myPhotos = Stream.of(photo1, photo2)
            .collect(Collectors.toCollection(HashSet::new));
    private Integer[] routeIds = new Integer[1];

    private Route route1;
    private Route route2;

    @BeforeEach
    void setUp(){
        route1 = new Route(1, myPhotos);
        route2 = new Route(2, myPhotos);
    }

    @AfterEach
    void tearDown() {
        routeRepo.deleteAll();
        route1 = null;
        route2 = null;
    }

    // Test that we can directly save a route to the repo and retrieve it
    @Test
    public void addAndRetrieveRoute(){
        routeRepo.save(route1);
        Route retrievedRoute = routeRepo.findById(route1.getId()).get();
        assertEquals(1, retrievedRoute.getId());
    }

//    // Test that we can use createOrGet() to add a route that does not yet exit in the repo
    @Test
    public void addNewRouteUsingCreateOrGet(){
        routeRepo.save(route1); // at this point repo contains only route 1
        routeIds[0] = route2.getId();
        routeRepo.createOrGet(routeIds); // now we want to add route 2. A Route with id 2 does not yet exist in the repo and should be added.
        // When the Route object is instantiated, the Set of Photos is empty.
        Route retrievedRoute = routeRepo.findById(route2.getId()).get();
        assertEquals(0, retrievedRoute.getPhotos().size());
    }


    // Test that we can use createOrGet() to retrieve a route that already exists in the repo
    @Test
    public void retrieveExistingRouteUsingCreateOrGet(){
        routeRepo.save(route1);
        routeRepo.save(route2);
        routeIds[0] = route2.getId();
        routeRepo.createOrGet(routeIds); // now we want to add route 2, which already exists in the repo.
        // In this case, createOrGet() should retrieve the existing Route object, containing 2 Photos.
        Route retrievedRoute = routeRepo.findById(route2.getId()).get();
        assertEquals(2, retrievedRoute.getPhotos().size());
    }

}
