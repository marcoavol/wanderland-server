package com.wanderland.wanderlandserver.config;

import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.transaction.Transactional;


/**
 * DEPRECATED: Populates repositories with test data
 *
 * @author Irene Keller

 */


@Configuration
@Profile("prod")
@Transactional
public class ProdConfiguration {

    Photo photo1;

    @Autowired
    PhotoRepository photoRepository;

//    @PostConstruct
    public void createData(){
        photo1 = createPhoto((float) 46.2, (float) 8.6);
        photoRepository.save(photo1);
        System.out.println("Photo wurde in die Datenbank gespeichert");
    }

    private Photo createPhoto(float lat, float lon) {
        Photo photo = new Photo();
        photo.setLat(lat);
        photo.setLon(lon);
        photo.setSrc(null);
        return photo;
    }

}
