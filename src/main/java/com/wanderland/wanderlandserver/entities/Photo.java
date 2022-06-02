//https://www.baeldung.com/jpa-many-to-many

package com.wanderland.wanderlandserver.entities;


import com.wanderland.wanderlandserver.services.GetPhotoDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
public class Photo {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float lon;
    private float lat;
    private String src;

    private String captureIsoDate;
    @ManyToMany(fetch = FetchType.EAGER)
    // FetchType.EAGER: macht dass Routen sofort zusammen mit den anderen Datenfeldern geladen werden (und nicht erst dann, wenn
    // sie tatsächlich benötigt werden.)

    // Definiert die joining table, die für many-to-many relationships benötigt wird
    @JoinTable(
            name = "linked_routes",
            joinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "route_id", referencedColumnName = "route_id", nullable = false, updatable = false)
    )
    Set<Route> routes = new HashSet<Route>();

    // Empty constructor
    public Photo() {
    }

    // Getters and setters

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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCaptureIsoDate() {
        return captureIsoDate;
    }
    public void setCaptureIsoDate(String captureIsoDate) {
        this.captureIsoDate = captureIsoDate;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public GetPhotoDTO convertToPhotoDTO(Photo photo){
        GetPhotoDTO getPhotoDTO = new GetPhotoDTO();
        getPhotoDTO.setLat(photo.getLat());
        getPhotoDTO.setLon(photo.getLon());
        getPhotoDTO.setSrc(photo.getSrc());

        // Das ist lächerlich. Das muss einfacher gehen
        List<Long> temp_route_ids =
                photo.getRoutes().stream()
                        .map(Route::getRoute_id)
                        .collect(Collectors.toList());
        Long[] route_ids = new Long[temp_route_ids.size()];
        for (int i = 0; i < temp_route_ids.size(); i++) {
            route_ids[i] = temp_route_ids.get(i);
        }
        getPhotoDTO.setRouteIds(route_ids);

        return getPhotoDTO;
    }


    @Override
    public String toString() {
        return  "Filename: "
                + this.src
                + "\n"
                + "Breite: "
                + this.lat
                + "\n"
                + "Länge: "
                + this.lon
                + "\n"
                + "Aufnahmedatum: "
                + this.captureIsoDate;
    }
}
