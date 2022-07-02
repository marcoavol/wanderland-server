package com.wanderland.wanderlandserver.repositories;
import com.wanderland.wanderlandserver.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JPA repository for routes.
 *
 * @author Marco Volken
 * @author Irene Keller
 */

public interface RouteRepository extends JpaRepository<Route, Integer> {

    /**
     * Retrieves all route instances from repository for given route identifiers.
     * If no route instance with a given identifier exists yet, it will be created first.
     * @param routeIds an array of identifiers for requested routes
     * @return a set of route instances corresponding to given route identifiers
     */
    @Transactional
    default Set<Route> createOrGet(Integer[] routeIds) {
        return Arrays.stream(routeIds).map(routeId -> {
            Optional<Route> existingRoute = this.findById(routeId);
            if (existingRoute.isPresent()) {
                return existingRoute.get();
            }
            Route route = new Route();
            route.setId(routeId);
            this.save(route);
            return route;
        }).collect(Collectors.toSet());
    }

}
