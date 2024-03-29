package com.example.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    @NotBlank(message = "UserName can not be empty!")
    private String name;
    @NotBlank(message = "Password can not be empty!")
    private String password;
}
