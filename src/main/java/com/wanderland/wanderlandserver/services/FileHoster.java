package com.wanderland.wanderlandserver.services;

import java.net.URL;

public interface FileHoster {

    public URL save(byte[] fileContent, String fileName) throws Exception;

}
