package com.example.demo.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ProductResponse;
import com.example.demo.DTO.ReviewDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.ReviewRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    ProductRepository productRepository;
//    ReviewRepository reviewRepository;
    UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository
//                          ReviewRepository reviewRepository
    ){
        this.productRepository = productRepository;
//        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }
    public ProductDTO createProduct(ProductDTO productDTO){
        Product product = new Product();
//        User owner = userRepository.findById(productDTO.getOwnerId()).orElseThrow(()->new RuntimeException("User not found:("));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
//        product.setOwner(owner);

        Product newProduct = productRepository.save(product);

        ProductDTO productResponse = new ProductDTO();
        productResponse.setId(newProduct.getId());
        productResponse.setName(newProduct.getName());
        productResponse.setDescription(newProduct.getDescription());
        productResponse.setPrice(newProduct.getPrice());
        return productResponse;
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

    public ProductDTO getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));
//        List<Review> reviews = reviewRepository.findByProductId(id);
//        product.setReviews(reviews);
        return mapToDTO(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        Product productToUpdate = productRepository.save(product);

        return mapToDTO(productToUpdate);
    }

    public String deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found:("));
        productRepository.delete(product);
        return "Success!";
    }

    private ProductDTO mapToDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
//        productDTO.setReviews(product.getReviews());
        return productDTO;
    }

    private Product mapToEntity(ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        return product;
    }

}
