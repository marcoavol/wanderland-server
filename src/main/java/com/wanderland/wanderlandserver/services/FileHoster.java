package com.wanderland.wanderlandserver.services;

import java.net.URL;

/**
 * Interface defining the required properties of a service that can host a file that is accessible through a (public) URL.
 *
 * @author Marco Volken
 */

public interface FileHoster {

    public URL hostFile(byte[] fileContent, String fileName) throws Exception;

}
