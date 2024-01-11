package com.example.demo.api.service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ProductResponse;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ProductService productService;


    @Test
    public void ProductService_CreateProduct_ReturnProductDTO(){
        User user = User.builder().username("test").password("test").build();
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        product.setOwner(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ProductDTO saved = productService.createProduct(productDTO);

        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void ProductService_GetAllProducts_ReturnAllProductDTO(){
        Page<Product> products = Mockito.mock(Page.class);

        when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(products);

        ProductResponse saved = productService.findAllProducts(1, 10);

        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void ProductService_GetProductById_ReturnProductDTO(){
        User user = User.builder().username("test").password("test").build();
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .owner(user)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        ProductDTO saved = productService.getProductById(1L);

        Assertions.assertThat(saved).isNotNull();
    }

    @Test
    public void ProductService_UpdateProduct_ReturnProductDTO(){
        User user = User.builder().username("test").password("test").build();
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .owner(user)
                .build();

        UserDTO userDTO = UserDTO.builder().name("test").password("test").build();
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .ownerId(userDTO.getId())
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        ProductDTO saved = productService.updateProduct(1L, productDTO);

        Assertions.assertThat(saved).isNotNull();
    }

 @Test
    public void ProductService_DeleteProduct_ReturnProductDTO(){
        User user = User.builder().username("test").password("test").build();
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .owner(user)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        ProductDTO saved = productService.getProductById(1L);

        assertAll(() -> productService.deleteProduct(1L));
    }
}
