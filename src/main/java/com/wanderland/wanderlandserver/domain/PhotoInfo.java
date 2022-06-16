package com.wanderland.wanderlandserver.domain;

/**
 * PhotoInfo contains coordinates where a photo was taken, the date when it was taken and an array with the IDs of associated routes
 *
 *
 * @author Marco Volken
 * @author Irene Keller

 */


public class PhotoInfo {

    private float lon;

    private float lat;

    private String captureIsoDate;

    private Integer[] routeIds;

    // Constructors
    public PhotoInfo(float lon, float lat, String captureIsoDate, Integer[] routeIds) {
        this.lon = lon;
        this.lat = lat;
        this.captureIsoDate = captureIsoDate;
        this.routeIds = routeIds;
    }

    public PhotoInfo() {

    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getCaptureIsoDate() {
        return captureIsoDate;
    }

    public void setcaptureIsoDate(String captureIsoDate) {
        this.captureIsoDate = captureIsoDate;
    }

    public Integer[] getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(Integer[] routeIds) {
        this.routeIds = routeIds;
    }

}
