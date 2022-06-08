package com.wanderland.wanderlandserver.repositories;
import com.wanderland.wanderlandserver.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface RouteRepository extends JpaRepository<Route, Integer> {

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
