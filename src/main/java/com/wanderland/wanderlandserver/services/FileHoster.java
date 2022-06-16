package com.wanderland.wanderlandserver.services;

import java.net.URL;



/**
 * Interface defining method to save photo files
 *
 * @author Marco Volken


 */


public interface FileHoster {

    public URL save(byte[] fileContent, String fileName) throws Exception;

}
