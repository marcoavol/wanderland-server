package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
