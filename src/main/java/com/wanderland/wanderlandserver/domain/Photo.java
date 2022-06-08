package com.wanderland.wanderlandserver.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Photo {

    // TODO: Use some kind of hash from photo byteArray and check on creation if photo with id already exists to prevent adding copies
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Photo() { }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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
        return  "src: "
                + this.src
                + "\n"
                + "lon: "
                + this.lon
                + "\n"
                + "lat: "
                + this.lat
                + "\n"
                + "captureIsoDate: "
                + this.captureIsoDate;
    }

}
