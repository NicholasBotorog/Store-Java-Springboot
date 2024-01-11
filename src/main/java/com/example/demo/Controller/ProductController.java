package com.example.demo.Controller;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ProductResponse;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;
    UserRepository userRepository;

    @Autowired
    public ProductController(ProductService productService, UserRepository userRepository){
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ){
        ProductResponse response = productService.findAllProducts(pageNo, pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO
//            , Authentication authentication
    ){
//        String username = authentication.getName();
//        User owner = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found:("));
        ProductDTO response = productService.createProduct(productDTO);
//        response.setOwner(owner);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(value="id") Long id){
        ProductDTO response = productService.getProductById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable(value="id") Long id, @RequestBody ProductDTO productDTO){
        ProductDTO response = productService.updateProduct(id,productDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value="id") Long id){
        return new ResponseEntity<>(productService.deleteProduct(id),HttpStatus.OK);
    }
}
