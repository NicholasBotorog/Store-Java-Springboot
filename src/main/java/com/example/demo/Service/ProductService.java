package com.example.demo.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ProductResponse;
import com.example.demo.DTO.ReviewDTO;
import com.example.demo.DTO.SingleProductDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository){
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO, String username){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        // Associate it with the Owner
        User owner = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found:("));
        product.setOwner(owner);

        Product newProduct = productRepository.save(product);

        return mapToDTO(newProduct);
    }


    public ProductResponse findAllProducts(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        List<Product> productList = products.getContent();
        List<ProductDTO> content = productList.stream().map(product -> mapToDTO(product)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalEl(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return productResponse;
    }

    public SingleProductDTO getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));
        return mapProductToDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws AccessDeniedException {
        validateProductOwner(id);

        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        Product productToUpdate = productRepository.save(product);

        return mapToDTO(productToUpdate);
    }

    @Transactional
    public String deleteProduct(Long id) throws AccessDeniedException {
        validateProductOwner(id);

        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));
        productRepository.delete(product);
        return "Success!";
    }

    private void validateProductOwner(Long productId) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(()-> new RuntimeException("User not found:("));
        Product product  = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("No Product here:("));

        if(!product.getOwner().equals(currentUser)){
            throw new AccessDeniedException("Unauthorized!");
        }
    }

    private ProductDTO mapToDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setOwnerId(product.getOwner().getId());
        return productDTO;
    }

    private SingleProductDTO mapProductToDTO(Product product){
        SingleProductDTO productDTO = new SingleProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setReviews(product.getReviews().stream().map(this::mapReviewToDTO).collect(Collectors.toList()));
        productDTO.setOwnerId(product.getOwner().getId());
        return productDTO;
    }

    private ReviewDTO mapReviewToDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setOwnerId(review.getOwner().getId());
        return reviewDTO;
    }

    private Product mapToEntity(ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        return product;
    }
}
