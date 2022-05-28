package com.wanderland.wanderlandserver.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Route {

        @Id
        @Column(name = "route_id", nullable = false)
        private int route_id;
        @ManyToMany(mappedBy = "routes")
        private Set<Photo> photos = new HashSet<Photo>();

        // Empty constructor
        public Route() {
        }

        // Getters and setters

        public int getRoute_id() {
                return route_id;
        }

        public void setRoute_id(int route_id) {
                this.route_id = route_id;
        }

        public Set<Photo> getPhotos() {
                return photos;
        }

        public void setPhotos(Set<Photo> photos) {
                this.photos = photos;
        }
}
