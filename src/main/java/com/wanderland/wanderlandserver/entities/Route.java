package com.wanderland.wanderlandserver.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Route {

        @Id
        @Column(name = "route_id", nullable = false)
        private Long route_id;
        @ManyToMany(mappedBy = "routes", fetch = FetchType.EAGER)
        private Set<Photo> photos = new HashSet<Photo>();

        // Empty constructor
        public Route() {
        }

        // Getters and setters

        public Long getRoute_id() {
                return route_id;
        }

        public void setRoute_id(Long route_id) {
                this.route_id = route_id;
        }

        public Set<Photo> getPhotos() {
                return photos;
        }

        public void addPhoto(Photo photo) {
                this.photos.add(photo);
        }

        @Override
        public String toString(){
               return "Routen-ID: " + this.route_id;
        }
}
