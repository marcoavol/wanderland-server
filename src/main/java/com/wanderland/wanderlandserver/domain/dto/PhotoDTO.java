package com.wanderland.wanderlandserver.domain.dto;

import com.wanderland.wanderlandserver.domain.PhotoInfo;

/**
 * Extends PhotoInfo to include the path to the photo on the file system.
 *
 * @author Marco Volken
 * @author Irene Keller
 */

public class PhotoDTO extends PhotoInfo {

    private String src;

    public PhotoDTO() { }

    public PhotoDTO(PhotoInfo photoInfo, String src) {
        super(photoInfo.getLon(), photoInfo.getLat(), photoInfo.getCaptureIsoDate(), photoInfo.getRouteIds());
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
