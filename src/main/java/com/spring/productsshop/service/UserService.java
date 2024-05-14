package com.spring.productsshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.productsshop.dto.UserDTO;
import com.spring.productsshop.exception.UsernameAlreadyExistsException;
import com.spring.productsshop.model.User;
import com.spring.productsshop.repository.UserRepository;
import com.spring.productsshop.util.PasswordUtil;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor que inyecta el repositorio UserRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método para obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Método para encontrar un usuario por su ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Método para guardar un usuario
    public User save(User user) {
        return userRepository.save(user);
    }

    // Método para verificar si existe un usuario por su ID
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // Método para eliminar un usuario por su ID
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Método para verificar si un nombre de usuario ya está tomado
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByName(username);
    }

    // Método para crear un nuevo usuario
    public User createUser(UserDTO userDTO) {
        if (isUsernameTaken(userDTO.getName())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + userDTO.getName());
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
        return userRepository.save(user);
    }

    // Método para encontrar un usuario por su nombre de usuario
    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    // Método para verificar las credenciales de inicio de sesión
    public boolean checkCredentials(String username, String rawPassword) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return PasswordUtil.checkPassword(rawPassword, user.getPassword());
        }
        return false;
    }
}

