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

    // M�todo para obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // M�todo para encontrar un usuario por su ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // M�todo para guardar un usuario
    public User save(User user) {
        return userRepository.save(user);
    }

    // M�todo para verificar si existe un usuario por su ID
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // M�todo para eliminar un usuario por su ID
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // M�todo para verificar si un nombre de usuario ya est� tomado
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByName(username);
    }

    // M�todo para crear un nuevo usuario
    public User createUser(UserDTO userDTO) {
        if (isUsernameTaken(userDTO.getName())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + userDTO.getName());
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
        return userRepository.save(user);
    }

    // M�todo para encontrar un usuario por su nombre de usuario
    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    // M�todo para verificar las credenciales de inicio de sesi�n
    public boolean checkCredentials(String username, String rawPassword) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return PasswordUtil.checkPassword(rawPassword, user.getPassword());
        }
        return false;
    }
}

