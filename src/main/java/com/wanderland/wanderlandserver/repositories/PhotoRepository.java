package com.wanderland.wanderlandserver.repositories;

import com.wanderland.wanderlandserver.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
