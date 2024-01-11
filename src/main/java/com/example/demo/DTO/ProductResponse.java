package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private List<ProductDTO> content;
    private int pageNo;
    private int pageSize;
    private long totalEl;
    private int totalPages;
    private boolean lastPage;
}
