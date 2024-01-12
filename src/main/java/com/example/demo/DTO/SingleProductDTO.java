package com.example.demo.DTO;

import com.example.demo.Entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleProductDTO {
    private Long id;
    public String name;
    private String description;
    private Double price;
    private List<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
    private Long ownerId;
}
