package com.wanderland.wanderlandserver.services;

public class GetPhotoDTO extends PhotoInfo {
    private String src;

    // empty constructor
    public GetPhotoDTO() {
        super();
    }

    //getter
    public String getSrc() {
        return src;
    }

    //setter
    public void setSrc(String src) {
        this.src = src;
    }
}
