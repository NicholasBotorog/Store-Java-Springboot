package com.example.demo.Service;

import com.example.demo.DTO.ReviewDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository){
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ReviewDTO addReview(Long productId, ReviewDTO reviewDTO, String username){
        User owner = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found:("));

        Review review = mapToEntity(reviewDTO);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found :("));

        review.setProduct(product);
        review.setOwner(owner);

        Review newReview = reviewRepository.save(review);

        return mapToDTO(newReview);
    }

    public List<ReviewDTO> findReviewByProductId(Long productId){
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream().map(review -> mapToDTO(review)).collect(Collectors.toList());
    }

    public ReviewDTO findReviewById(Long reviewId, Long productId){
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("No Product here:("));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new RuntimeException("No Review here:("));

        if(review.getProduct().getId() != product.getId()){
            throw new RuntimeException("Review not belong to this product");
        }

        return mapToDTO(review);
    }

    @Transactional
    public ReviewDTO updateReview(Long productId, Long id, ReviewDTO reviewDTO) throws AccessDeniedException {
        validateReviewOwner(productId, id);

        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found:("));
        Review review = reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review not found:("));

        if(review.getProduct().getId() != product.getId()){
            throw new RuntimeException("Review does not belong to this product");
        }

        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());

        Review reviewUpdate = reviewRepository.save(review);
        return mapToDTO(reviewUpdate);
    }

    @Transactional
    public String deleteReview(Long productId, Long reviewId) throws AccessDeniedException {
        validateReviewOwner(productId, reviewId);

        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found:("));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new RuntimeException("Review not found:("));
        if(review.getProduct().getId() != product.getId()){
            throw new RuntimeException("Review does not belong to this product");
        }
        reviewRepository.delete(review);
        return "Success!";
    }

    public long total(){
        return reviewRepository.count();
    }

    private void validateReviewOwner(Long productId, Long id) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(()-> new RuntimeException("User not found:("));
        Review review = reviewRepository.findByIdAndProductId(id, productId).orElseThrow(() -> new RuntimeException("No Review here:("));

        if(!review.getOwner().equals(currentUser)){
            throw new AccessDeniedException("Unauthorized!");
        }
    }

    private ReviewDTO mapToDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setOwnerId(review.getOwner().getId());
        return reviewDTO;
    }

    private Review mapToEntity(ReviewDTO reviewDTO){
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        return review;
    }
}
