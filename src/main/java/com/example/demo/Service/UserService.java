package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
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

    public UserDTO updateUser(Long id, UserDTO userDTO) throws AccessDeniedException {
        validateUserIsOwner(id);

        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found:("));
        user.setUsername(userDTO.getName());
        User userToUpdate = userRepository.save(user);
        return mapToDTO(userToUpdate);
    }

    public String deleteUser(Long id) throws AccessDeniedException {
        validateUserIsOwner(id);

        userRepository.deleteById(id);
        return "Success!";
    }

    private void validateUserIsOwner(Long id) throws AccessDeniedException {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(()-> new RuntimeException("User not found:("));
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found:("));

        if(!user.equals(currentUser)){
            throw new AccessDeniedException("Unauthorized!");
        }
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setName(user.getUsername());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

}
