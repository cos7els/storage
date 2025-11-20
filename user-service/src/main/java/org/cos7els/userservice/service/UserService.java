package org.cos7els.userservice.service;

import org.cos7els.userservice.dto.CreateUserDto;
import org.cos7els.userservice.dto.UserDto;
import org.cos7els.userservice.model.User;
import org.cos7els.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto createUser(CreateUserDto createUserDto) {
        // Проверяем, существует ли пользователь с таким именем или email
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Создаем нового пользователя
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword()); // В реальном приложении нужно хэшировать пароль
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return convertToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id, CreateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Проверяем, не используется ли уже новый username или email другим пользователем
        if (!user.getUsername().equals(updateUserDto.getUsername()) 
                && userRepository.existsByUsername(updateUserDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (!user.getEmail().equals(updateUserDto.getEmail()) 
                && userRepository.existsByEmail(updateUserDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        user.setPassword(updateUserDto.getPassword()); // В реальном приложении нужно хэшировать пароль
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}