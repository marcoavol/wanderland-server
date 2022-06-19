package com.wanderland.wanderlandserver.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Route saves a route with all associated photos to the repository.
 * There is a many-to-many relationship between routes and photos.
 *
 * @author Marco Volken
 * @author Irene Keller
 */

@Entity
public class Route {

        @Id
        @Column(nullable = false, unique = true, updatable = false)
        private Integer id;

        @ManyToMany(mappedBy = "routes", fetch = FetchType.EAGER)
        private Set<Photo> photos = new HashSet<Photo>();

        public Route() { }

        /**
         * @param id the unique identifier of the instance
         * @param photos a Set containing all Photo instances to which this Route instance shall be associated with
         */
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
