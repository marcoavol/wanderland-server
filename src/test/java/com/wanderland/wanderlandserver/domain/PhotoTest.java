package com.wanderland.wanderlandserver.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    private Photo photo;
    private Route route = new Route();
    Set<Route> myRoutes = Stream.of(route)
            .collect(Collectors.toCollection(HashSet::new));


    @BeforeEach
    void setUp() {
        photo = new Photo(1L, 9.4928f, 46.80982f, "2020-07-10 15:00:00.000", "myphoto.jpg", myRoutes);
    }

    @AfterEach
    void tearDown() {
        photo = null;
    }

    // Test that new routes can be added to Photo object
    @Test
    public void addNewRoutes(){
        photo.addRoutes(myRoutes);
        assertEquals(1, photo.getRoutes().size());
    }



}
