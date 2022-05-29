package com.wanderland.wanderlandserver.services;

public class PhotoDTO {
        private String captureIsoDate;
        private float lon;
        private float lat;
        private Long[] routeIds;

    public String getCaptureIsoDate() {
        return captureIsoDate;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public Long[] getRouteIds() {
        return routeIds;
    }
}
