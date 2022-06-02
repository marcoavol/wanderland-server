package com.wanderland.wanderlandserver.services;

public class PhotoInfo {
        private String captureIsoDate;
        private float lon;
        private float lat;
        private Long[] routeIds;

        // Getter
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

    // Setter

    public void setCaptureIsoDate(String captureIsoDate) {
        this.captureIsoDate = captureIsoDate;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setRouteIds(Long[] routeIds) {
        this.routeIds = routeIds;
    }
}
