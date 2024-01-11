package com.example.demo.api.repository;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void ReviewRepository_SaveAll_ReturnSavedReview(){
        Review review = Review.builder().content("The best test!").rating(5).build();

        Review saved = reviewRepository.save(review);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void ReviewRepository_GetAll_ReturnMoreThanOne(){
        Review review1 = Review.builder().content("The best test!").rating(5).build();
        Review review2 = Review.builder().content("The second best test!").rating(3).build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviewsList = reviewRepository.findAll();
        Assertions.assertThat(reviewsList).isNotNull();
        Assertions.assertThat(reviewsList.size()).isGreaterThan(1);
        Assertions.assertThat(reviewsList.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_ReturnReviewById(){
        Review review = Review.builder().content("The best test!").rating(5).build();
        reviewRepository.save(review);

        Review reviewById = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(reviewById).isNotNull();
    }

    @Test
    public void ReviewRepository_FindByProductId_ReturnReviewByProductId(){
        Review review = Review.builder().content("The best test!").rating(5).build();
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        review.setProduct(product);
        reviewRepository.save(review);
        productRepository.save(product);

        List<Review> reviewByProductId = reviewRepository.findByProductId(product.getId());

        Assertions.assertThat(reviewByProductId).isNotNull();
        Assertions.assertThat(
                reviewByProductId.stream().map(r -> r.getProduct().getId()).toList().get(0))
                .isEqualTo(product.getId());
    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnReviewUpdated(){
        Review review = Review.builder().content("Content").rating(5).build();
        reviewRepository.save(review);

        Review reviewSaved = reviewRepository.findById(review.getId()).get();
        reviewSaved.setContent("Content UPDATED");
        reviewSaved.setRating(4);

        Review updated = reviewRepository.save(reviewSaved);
        Assertions.assertThat(updated.getContent()).isNotNull();
        Assertions.assertThat(updated.getRating()).isNotNull();
    }

    @Test
    public void ReviewRepository_DeleteReview_ReturnReviewNotNull() {
        Review review = Review.builder().content("Content").rating(5).build();

        reviewRepository.save(review);
        reviewRepository.deleteById(review.getId());

        Optional<Review> reviewReturn = reviewRepository.findById(review.getId());

        Assertions.assertThat(reviewReturn).isEmpty();
    }
}
