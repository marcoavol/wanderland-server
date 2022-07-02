package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Irene Keller
 */

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

    /**
     * Tests if a Route object with a given ID can be retrieved from the repository after it has been added.
     */
    @Test
    public void addAndRetrieveRoute(){
        routeRepo.save(route1);
        Route retrievedRoute = routeRepo.findById(route1.getId()).get();
        assertEquals(1, retrievedRoute.getId());
    }

    /**
     * Tests what happens when a Route object that does not yet exist in the repository is added
     * The Route objects instantiated above both have 2 Photo objects associated with them
     * In contrast, a Route object that is instantiated by createOrGet() will not have any Photos associated with it yet
     * We can use this to distinguish between existing and newly created Route objects.
     */
    @Test
    public void addNewRouteUsingCreateOrGet(){
        // Add route1 instantiated above to the repository
        routeRepo.save(route1);
        // Add a second Route object with ID 2. This does not yet exist in the repository and a new Route object should be instantiated.
        routeIds[0] = route2.getId();
        routeRepo.createOrGet(routeIds);
        // We expect that a new Route should not yet have any Photo objects associated with it.
        Route retrievedRoute = routeRepo.findById(route2.getId()).get();
        assertEquals(0, retrievedRoute.getPhotos().size());
    }

    /**
     * Test what happens when we try to add a Route object that already exists in the repository
     * The rationale of the test is the same as outlined for the previous test
     */
    @Test
    public void retrieveExistingRouteUsingCreateOrGet(){
        // Add both Route objects instantiated above to the repository
        routeRepo.save(route1);
        routeRepo.save(route2);
        // Try to add a Route with ID 2, which already exists in the repository
        // In this case, createOrGet() should retrieve the existing Route object, containing 2 Photos.
        routeIds[0] = route2.getId();
        routeRepo.createOrGet(routeIds);
        Route retrievedRoute = routeRepo.findById(route2.getId()).get();
        assertEquals(2, retrievedRoute.getPhotos().size());
    }
}
