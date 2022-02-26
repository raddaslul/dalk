package com.dalk.repository;

import com.dalk.domain.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarouselRepository extends JpaRepository<Carousel, Long> {
}