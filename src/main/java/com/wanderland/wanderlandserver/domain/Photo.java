package com.wanderland.wanderlandserver.domain;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Photo saves information on where and when a photo was taken, where it is saved and which routes it is located on to the repository.
 * There is a many-to-many relationship between photos and routes.
 *
 * @author Marco Volken
 * @author Irene Keller

 */

@Entity
public class Photo {

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    private float lon;

    private float lat;

    private String captureIsoDate;

    private String src;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "photo_route",
            joinColumns = @JoinColumn(name = "photo_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "route_id", nullable = false, updatable = false)
    )
    Set<Route> routes = new HashSet<Route>();

    // Empty constructor
    public Photo() { }


/**
 * @param id Photo identifier
 * @param   lon Longitude where photo was taken in decimal degrees
 * @param   lat Latitude where photo was taken in decimal degrees
 * @param captureIsoDate    Date when photo was taken
 * @param src   Path to the jpg file on the file system
 * @param routes    HashSet containing route(s) overlapping with the location of the photo

 */
 // Constructor with all instance variables. Required for unit tests
    public Photo(Long id, float lon, float lat, String captureIsoDate, String src, Set<Route> routes) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.captureIsoDate = captureIsoDate;
        this.src = src;
        this.routes = routes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCaptureIsoDate(String captureIsoDate) {
        this.captureIsoDate = captureIsoDate;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void addRoutes(Collection<Route> routes) {
        this.routes.addAll(routes);
        routes.forEach(route -> route.addPhoto(this));
    }

    @Override
    public String toString() {
        return  "src: " + this.src
                + "\n"
                + "lon: " + this.lon
                + "\n"
                + "lat: " + this.lat
                + "\n"
                + "captureIsoDate: " + this.captureIsoDate;
    }

}
