package com.wanderland.wanderlandserver.services;

import org.springframework.web.multipart.MultipartFile;

public class CreatePhotoDTO extends PhotoDTO {
    private MultipartFile photo;

    // Getter
    public MultipartFile getPhoto() {
        return photo;
    }
}
