package com.wanderland.wanderlandserver.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Route {

        @Id
        @Column(name = "id", nullable = false, unique = true, updatable = false)
        private Integer id;

        @ManyToMany(mappedBy = "routes", fetch = FetchType.EAGER)
        private Set<Photo> photos = new HashSet<Photo>();

        public Route() { }

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
               return "RouteId: " + this.id;
        }

}
