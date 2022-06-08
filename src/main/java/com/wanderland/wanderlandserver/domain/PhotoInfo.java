package com.wanderland.wanderlandserver.domain;

public class PhotoInfo {

    private float lon;

    private float lat;

    private String captureIsoDate;

    private Integer[] routeIds;

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
