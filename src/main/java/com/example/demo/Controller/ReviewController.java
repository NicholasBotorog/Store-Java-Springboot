package com.example.demo.Controller;

import com.example.demo.DTO.ReviewDTO;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.Service.ReviewService;
import com.example.demo.Service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ReviewController {
    private ReviewService reviewService;
    private UserService userService;

    @Autowired
    public ReviewController(UserService userService, ReviewService reviewService){
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReviewDTO> postReview(@PathVariable(value="productId") Long productId, @RequestBody ReviewDTO reviewDTO, Principal principal){
        String username = principal.getName();
        return new ResponseEntity<>(reviewService.addReview(productId, reviewDTO, username), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/reviews")
    public List<ReviewDTO> getReviewByProduct(@PathVariable(value="productId") Long productId){
        return reviewService.findReviewByProductId(productId);
    }

    @GetMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable(value="productId") Long productId, @PathVariable(value="reviewId") Long reviewId){
        ReviewDTO response = reviewService.findReviewById(reviewId, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable(value="productId") Long productId, @PathVariable(value="reviewId") Long reviewId, @RequestBody ReviewDTO reviewDTO) throws AccessDeniedException {
//        User currUser = userService.getUserByName(principal.getName());
//        return reviewService.updateReview(productId, id, review);
        ReviewDTO response = reviewService.updateReview(productId, reviewId, reviewDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable(value="productId") Long productId,@PathVariable(value="reviewId") Long reviewId) throws AccessDeniedException {
//        User currUser = userService.getUserByName(principal.getName());
        return new ResponseEntity<>(reviewService.deleteReview(productId, reviewId) ,HttpStatus.OK);
    }

}
