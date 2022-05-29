// Speichert ein "Photo" in der PostgreSQL db

package com.wanderland.wanderlandserver.config;


import com.wanderland.wanderlandserver.entities.Photo;
import com.wanderland.wanderlandserver.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Configuration
@Profile("prod")
@Transactional // Stellt sicher, dass entweder alle oder keine der folgenden db Operationen durchgeführt werden
public class ProdConfiguration {

    Photo photo1;


    @Autowired
    PhotoRepository photoRepository;


    @PostConstruct
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
