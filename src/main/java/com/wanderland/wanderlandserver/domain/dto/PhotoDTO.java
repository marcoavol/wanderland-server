package com.wanderland.wanderlandserver.domain.dto;

import com.wanderland.wanderlandserver.domain.PhotoInfo;


/**
 * Extends PhotoInfo to include the path to the photo on the file system
 *
 * @author Marco Volken
 * @author Irene Keller

 */


public class PhotoDTO extends PhotoInfo {

    private String src;

    public PhotoDTO() {
        super();
    }

    public PhotoDTO(PhotoInfo photoInfo, String s) {
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
