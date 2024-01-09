package com.example.demo.Repository;

import com.example.demo.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    Optional<Review> findByIdAndProductId(Long id, Long productId);
}
