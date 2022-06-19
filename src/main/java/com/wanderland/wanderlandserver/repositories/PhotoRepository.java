package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * JPA repository for photos.
 *
 * @author Marco Volken
 * @author Irene Keller
 */

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    boolean existsByCaptureIsoDateAndLonAndLat(String captureIsoDate, float lon, float lat);

}
