package com.wanderland.wanderlandserver.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


// A Route object contains an ID and a set if photos associated with the route
@Entity
public class Route {

        @Id
        @Column(name = "id", nullable = false, unique = true, updatable = false)
        private Integer id;

        @ManyToMany(mappedBy = "routes", fetch = FetchType.EAGER)
        private Set<Photo> photos = new HashSet<Photo>();

        // empty constructor
        public Route() { }

        // constructor with instance variables (required by unit tests)
        public Route(Integer id, Set<Photo> photos) {
                this.id = id;
                this.photos = photos;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer route_id) {
                this.id = route_id;
        }

        public Set<Photo> getPhotos() {
                return photos;
        }

        public void addPhoto(Photo photo) {
                this.photos.add(photo);
        }

        @Override
        public String toString(){
               return "RouteId: " + this.id
                       + "\n";


        }

}
