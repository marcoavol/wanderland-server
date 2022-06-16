

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
     *
     * @param photoInfo
     * @return  Identifier for photo generated from capture date and coordinates.
     */
    public Long generateId(PhotoInfo photoInfo) {
        String infoStr = photoInfo.getCaptureIsoDate() + photoInfo.getLon() + photoInfo.getLat();
        ByteBuffer buffer = ByteBuffer.allocate(infoStr.getBytes().length);
        buffer.put(infoStr.getBytes());
        buffer.flip();
        return buffer.getLong();
    }

    /**
     * Converter from Photo to PhotoDTO object
     * @param photo
     * @return PhotoDTO
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
     *
     * @param src       path to photo on file system
     * @param photoInfo PhotoInfo object
     * @param routes    Set containing Route object(s) associated with the photo
     * @return Photo
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
     *
     * @param photoFile MultipartFile
     * @return URL
     * @throws Exception
     */
    public URL saveFile(MultipartFile photoFile) throws Exception {
        return this.photoHosterService.save(
                photoFile.getBytes(),
                StringUtils.cleanPath(photoFile.getOriginalFilename())
        );
    }

}
