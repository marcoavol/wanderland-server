package com.wanderland.wanderlandserver.services;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.PhotoInfo;
import com.wanderland.wanderlandserver.domain.Route;
import com.wanderland.wanderlandserver.domain.dto.PhotoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Irene Keller
 */

class PhotoServiceTest {

    private Photo photo;
    private Route route1 = new Route();
    private Route route2 = new Route();
    Set<Photo> myPhotos = Stream.of(photo)
            .collect(Collectors.toCollection(HashSet::new));
    private PhotoInfo photoInfo;
    private PhotoService photoService = new PhotoService();

    @BeforeEach
    void setUp() {
        route1 = new Route(1, myPhotos);
        route2 = new Route(2, myPhotos);
        Set<Route> myRoutes = Stream.of(route1, route2)
                .collect(Collectors.toCollection(HashSet::new));
        photo = new Photo(1L, 9.4928f, 46.80982f, "2020-07-10 15:00:00.000", "myphoto.jpg", myRoutes);
        Integer[] myroutes = {1, 2};
        photoInfo = new PhotoInfo(10.4928f, 47.80982f, "1999-07-10 15:00:00.000", myroutes);
    }

    @AfterEach
    void tearDown() {
        route1 = null;
        route2 = null;
        photo = null;
        photoInfo = null;
    }

    /**
     * The most complex step when converting a Photo to a PhotoDTO object, is to convert the HashSet<Route> to an Array containing only the route IDs.
     * Here we test if the number of IDs and their values correspond to our expectation based on setUp()
     * A disadvantage of this strategy is that the test can fail for more than one reason
     */
    @Test
    void toDTO() {
        PhotoDTO photoDTO = photoService.toDTO(photo);
        Integer[] dto_routes = photoDTO.getRouteIds();
        Arrays.sort(dto_routes);
        assertTrue((dto_routes.length == 2) && (dto_routes[0] == 1) && (dto_routes[1] == 2));
    }

    /**
     * In this test, we extract a data field from the newly created Photo object and test if the value matches our expectation based on setUp()
     */
    @Test
    void toPhoto() {
        Photo testPhoto = photoService.toPhoto("myfotofile.jpg", photoInfo, Stream.of(route1, route2)
                .collect(Collectors.toCollection(HashSet::new)));
        assertEquals(testPhoto.getCaptureIsoDate(), "1999-07-10 15:00:00.000");
    }
}
