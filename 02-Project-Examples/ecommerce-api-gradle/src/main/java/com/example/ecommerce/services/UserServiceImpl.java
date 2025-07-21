package com.example.ecommerce.services;

import com.example.ecommerce.dtos.user.UserCreateDto;
import com.example.ecommerce.dtos.user.UserResponseDto;
import com.example.ecommerce.dtos.user.UserUpdateDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserResponseDto createUser(UserCreateDto dto) {
        User user = new User();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(com.example.ecommerce.utils.PasswordUtil.encode(dto.getPassword()));
        user.setActive(dto.isActive());
        user.setRoles(java.util.Arrays.stream(dto.getRoles()).collect(java.util.stream.Collectors.toSet()));
        user = userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) return null;
        User user = opt.get();
        if (dto.getFullname() != null) {
            user.setFullname(dto.getFullname());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(com.example.ecommerce.utils.PasswordUtil.encode(dto.getPassword()));
        }
        if (dto.isActive() != null) {
            user.setActive(dto.isActive());
        }
        if (dto.getRoles() != null) {
            user.setRoles(java.util.Arrays.stream(dto.getRoles()).collect(java.util.stream.Collectors.toSet()));
        }
        user = userRepository.save(user);
        return toDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDto).orElse(null);
    }

    public UserResponseDto verifyUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (com.example.ecommerce.utils.PasswordUtil.matches(password, user.getPassword())) {
                return toDto(user);
            }
        }
        return null; // or throw an exception
    }


    private UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFullname(user.getFullname());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setRoles(user.getRoles());
        return dto;
    }
}
