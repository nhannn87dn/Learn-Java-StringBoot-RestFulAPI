package com.example.ecommerce.services;

import com.example.ecommerce.dtos.user.*;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userRequestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserUpdateDto userRequestDto);
    void deleteUser(Long id);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto verifyUser(String email, String password);
}
