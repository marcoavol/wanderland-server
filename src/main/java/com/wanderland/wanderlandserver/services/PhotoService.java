package com.wanderland.wanderlandserver.services;

import com.wanderland.wanderlandserver.domain.dto.PhotoDTO;
import com.wanderland.wanderlandserver.domain.PhotoInfo;
import com.wanderland.wanderlandserver.domain.Photo;
import com.wanderland.wanderlandserver.domain.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.Collection;

@Service
public class PhotoService {

    @Autowired
    FileHoster photoHosterService;

    public PhotoDTO toDTO(Photo photo){
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setLat(photo.getLat());
        photoDTO.setLon(photo.getLon());
        photoDTO.setSrc(photo.getSrc());
        photoDTO.setRouteIds(photo.getRoutes().stream().map(Route::getId).toArray(Integer[]::new));
        return photoDTO;
    }

    public Photo toPhoto(String src, PhotoInfo photoInfo, Collection<Route> routes) throws Exception {
        Photo photo = new Photo();
        photo.setSrc(src);
        photo.setLon(photoInfo.getLon());
        photo.setLat(photoInfo.getLat());
        photo.setCaptureIsoDate(photoInfo.getCaptureIsoDate());
        photo.addRoutes(routes);
        return photo;
    }

    public URL saveFile(MultipartFile photoFile) throws Exception {
        return this.photoHosterService.save(
                photoFile.getBytes(),
                StringUtils.cleanPath(photoFile.getOriginalFilename())
        );
    }

}
