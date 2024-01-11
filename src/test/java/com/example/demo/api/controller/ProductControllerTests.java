package com.example.demo.api.controller;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.ProductResponse;
import com.example.demo.DTO.ReviewDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.Review;
import com.example.demo.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(controllers = ProductControllerTests.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

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
                .name("Test ProductDTO")
                .description("This is a test!")
                .price(10.00)
                .build();

        review = Review.builder().content("Content").rating(5).build();
        reviewDTO = ReviewDTO.builder().content("Content").rating(5).build();
    }

    @Test
    public void ProductController_CreateProduct_ReturnCreated() throws Exception {
        given(productService.createProduct(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/products/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(productDTO.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(productDTO.getPrice())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_GetAllProducts_ReturnResponseDTO() throws Exception{
        ProductResponse responseDTO = ProductResponse.builder().pageNo(1).pageSize(10).lastPage(true).content(Arrays.asList(productDTO)).build();
        when(productService.findAllProducts(1,10)).thenReturn(responseDTO);

        ResultActions response = mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDTO.getContent().size())))
                .andDo(MockMvcResultHandlers.print());
    }
}
