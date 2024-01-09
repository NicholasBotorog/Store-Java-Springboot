package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDTO> fetchAll(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> mapToDTO(user)).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("No user :("));
        return mapToDTO(user);
    }

//    public UserDTO getUserByName(String name){
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("No user with that name:("));
//        return mapToDTO(user);
//    }

    public UserDTO updateUser(Long id, UserDTO userDTO){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found:("));
        user.setUsername(userDTO.getName());
        User userToUpdate = userRepository.save(user);
        return mapToDTO(userToUpdate);
    }

    public String deleteUser(Long id){
        userRepository.deleteById(id);
        return "Success!";
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setName(user.getUsername());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

}
