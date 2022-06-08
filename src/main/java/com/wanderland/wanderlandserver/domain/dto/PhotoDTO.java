package com.wanderland.wanderlandserver.domain.dto;

import com.wanderland.wanderlandserver.domain.PhotoInfo;

public class PhotoDTO extends PhotoInfo {

    private String src;

    public PhotoDTO() {
        super();
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
