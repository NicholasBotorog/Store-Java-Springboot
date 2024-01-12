package com.example.demo.api.service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ReviewDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ReviewService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReviewService reviewService;


    private Product product;
    private Review review;
    private ReviewDTO reviewDTO;
    private ProductDTO productDTO;

    @BeforeEach
    public void init() {
        product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        review = Review.builder().content("Content").rating(5).build();
        reviewDTO = ReviewDTO.builder().content("Content").rating(5).build();
    }

    @Test
    public void ReviewService_CreateReview_ReturnReviewDTO(){
        User user = User.builder().username("test").password("test").build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDTO saved = reviewService.addReview(product.getId(), reviewDTO, user.getUsername());

        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void ReviewService_FindReviewByProductId_ReturnReviewDTO(){
        long productId = 1;
        when(reviewRepository.findByProductId(productId)).thenReturn(Arrays.asList(review));

        List<ReviewDTO> reviewReturn = reviewService.findReviewByProductId(productId);

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_FindReviewById_ReturnReviewDTO(){
        long reviewId = 1;
        long productId = 1;

        review.setProduct(product);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ReviewDTO reviewReturn = reviewService.findReviewById(reviewId, productId);

        Assertions.assertThat(reviewReturn).isNotNull();
    }

    @Test
    public void ReviewService_UpdateReview_ReturnReviewDTO() throws AccessDeniedException {
        long reviewId = 1;
        long productId = 1;

        product.setReviews(Arrays.asList(review));
        review.setProduct(product);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.save(review)).thenReturn(review);

        ReviewDTO reviewUpdate = reviewService.updateReview(productId, reviewId, reviewDTO);

        Assertions.assertThat(reviewUpdate).isNotNull();
    }    
    
    @Test
    public void ReviewService_DeleteReview_ReturnReviewDTO(){
        long reviewId = 1;
        long productId = 1;

        product.setReviews(Arrays.asList(review));
        review.setProduct(product);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertAll(() -> reviewService.deleteReview(productId, reviewId));
    }

}
