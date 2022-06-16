package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Irene Keller
 */

@ExtendWith(SpringExtension.class) // integrates the Spring test context framework
@DataJpaTest
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepo;

    private Photo photo1;

    private Photo photo2;

    private Route route1;

    Set<Route> myRoutes = Stream.of(route1)
            .collect(Collectors.toCollection(HashSet::new));

    @BeforeEach
    void setUp() {
        photo1 = new Photo(1L, 9.4928f, 46.80982f, "2020-07-10 15:00:00.000", "myphoto.jpg", myRoutes);
        photo2 = new Photo(2L, 9.87f, 45.89802f, "2020-07-10 22:00:00.000", "mysecondphoto.jpg", myRoutes);
    }

    @AfterEach
    void tearDown() {
        photoRepo.deleteAll();
        photo1 = null;
        photo2 = null;
        route1 = null;
    }

    @Test
    public void addAndRetrievePhoto(){
        photoRepo.save(photo1);
        Photo retrievedPhoto = photoRepo.findById(photo1.getId()).get();
        assertEquals(1, retrievedPhoto.getId());
    }

    @Test
    public void retrieveListOfPhotos(){
        photoRepo.save(photo1);
        photoRepo.save(photo2);
        List<Photo> photoList = (List<Photo>) photoRepo.findAll();
        assertEquals(2, photoList.size());
    }

}
