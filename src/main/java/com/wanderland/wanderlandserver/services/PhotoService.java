

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
import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author Marco Volken
 * @author Irene Keller
 */

@Service
public class PhotoService {

    @Autowired
    FileHoster photoHosterService;

    /**
     * Generates an identifier for a Photo instance based on its captureIsoDate and coordinates.
     * @param photoInfo the required information about the photo
     * @return an identifier for a Photo instance
     */
    public Long generateId(PhotoInfo photoInfo) {
        String infoStr = photoInfo.getCaptureIsoDate() + photoInfo.getLon() + photoInfo.getLat();
        ByteBuffer buffer = ByteBuffer.allocate(infoStr.getBytes().length);
        buffer.put(infoStr.getBytes());
        buffer.flip();
        return buffer.getLong();
    }

    /**
     * Maps a Photo instance to an instance of PhotoDTO.
     * @param photo the Photo instance
     * @return an instance of PhotoDTO corresponding to given Photo instance
     */
    public PhotoDTO toDTO(Photo photo){
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setLat(photo.getLat());
        photoDTO.setLon(photo.getLon());
        photoDTO.setSrc(photo.getSrc());
        photoDTO.setRouteIds(photo.getRoutes().stream().map(Route::getId).toArray(Integer[]::new));
        return photoDTO;
    }

    /**
     * Creates a Photo instance.
     * @param src a String of a (public) URL to the hosted photo
     * @param photoInfo information about the photo
     * @param routes a Set containing all Route instances to associate with this Photo instance
     * @return the created Photo instance
     */
    public Photo toPhoto(String src, PhotoInfo photoInfo, Collection<Route> routes) {
        Photo photo = new Photo();
        photo.setId(this.generateId(photoInfo));
        photo.setLon(photoInfo.getLon());
        photo.setLat(photoInfo.getLat());
        photo.setCaptureIsoDate(photoInfo.getCaptureIsoDate());
        photo.setSrc(src);
        photo.addRoutes(routes);
        return photo;
    }

    /**
     * A convenience method to allow for (implementation independent) hosting of a photo file.
     * @param photoFile the photo file
     * @return URL
     * @throws Exception
     */
    public URL saveFile(MultipartFile photoFile) throws Exception {
        return this.photoHosterService.hostFile(
                photoFile.getBytes(),
                StringUtils.cleanPath(photoFile.getOriginalFilename())
        );
    }

}
