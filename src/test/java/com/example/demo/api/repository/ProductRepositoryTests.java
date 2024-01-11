package com.example.demo.api.repository;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;
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
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void ProductRepository_SaveAll_ReturnSavedProduct(){
        // Arrange
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        // Act
        Product saveProduct = productRepository.save(product);

        // Assert
        Assertions.assertThat(saveProduct).isNotNull();
        Assertions.assertThat(saveProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepository_GetAll_ReturnMoreThanOne(){
        Product product1 = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        Product product2 = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        List<Product> productList = productRepository.findAll();

        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isGreaterThan(1);
        Assertions.assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepository_FindById_ReturnProductById(){
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        productRepository.save(product);

        Product productList = productRepository.findById(product.getId()).get();

        Assertions.assertThat(productList).isNotNull();
    }

    @Test
    public void ProductRepository_UpdateProduct_ReturnProductUpdated() {
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        productRepository.save(product);

        Product productSaved = productRepository.findById(product.getId()).get();
        productSaved.setPrice(55.55);
        productSaved.setDescription("Test for Update!");
        Product updatedProduct = productRepository.save(productSaved);

        Assertions.assertThat(updatedProduct.getDescription()).isNotNull();
        Assertions.assertThat(updatedProduct.getPrice()).isNotNull();
    }

    @Test
    public void ProductRepository_DeleteProduct_ReturnProductNotNull() {
        Product product = Product.builder()
                .name("Test Product")
                .description("This is a test!")
                .price(10.00)
                .build();

        productRepository.save(product);
        productRepository.deleteById(product.getId());

        Optional<Product> productReturn = productRepository.findById(product.getId());

        Assertions.assertThat(productReturn).isEmpty();
    }


    }
