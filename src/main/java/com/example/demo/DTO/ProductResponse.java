package com.example.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private List<ProductDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalEl;
    private int totalPages;
    private boolean lastPage;
}
