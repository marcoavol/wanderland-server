package com.wanderland.wanderlandserver.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Irene Keller
 */

class RouteTest {

    private Photo photo1 = new Photo();

    private Route route1;

    Set<Photo> myPhotos = Stream.of(photo1)
            .collect(Collectors.toCollection(HashSet::new));

    @BeforeEach
    void setUp(){
        route1 = new Route(1, myPhotos);
    }

    @AfterEach
    void tearDown(){
        route1 = null;
    }

    @Test
    public void addNewPhoto(){
        route1.addPhoto(new Photo());
        assertEquals(2, route1.getPhotos().size());
    }

}
