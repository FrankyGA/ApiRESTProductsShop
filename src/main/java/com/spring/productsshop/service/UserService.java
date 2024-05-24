package com.spring.productsshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.productsshop.dto.UserDTO;
import com.spring.productsshop.exception.UsernameAlreadyExistsException;
import com.spring.productsshop.model.User;
import com.spring.productsshop.repository.UserRepository;
import com.spring.productsshop.util.PasswordUtil;


@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor que inyecta el repositorio UserRepository
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Método para obtener todos los usuarios.
     *
     * @return Una lista de todos los usuarios.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Método para encontrar un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, de lo contrario está vacío.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Método para guardar un usuario.
     *
     * @param user El usuario a guardar.
     * @return El usuario guardado.
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Método para verificar si existe un usuario por su ID.
     *
     * @param id El ID del usuario a verificar.
     * @return true si el usuario existe, de lo contrario false.
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Método para eliminar un usuario por su ID.
     *
     * @param id El ID del usuario a eliminar.
     */
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Método para verificar si un nombre de usuario ya está tomado.
     *
     * @param username El nombre de usuario a verificar.
     * @return true si el nombre de usuario está tomado, de lo contrario false.
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByName(username);
    }

    /**
     * Método para crear un nuevo usuario.
     *
     * @param userDTO Los datos del usuario a crear.
     * @return El usuario creado.
     * @throws UsernameAlreadyExistsException si el nombre de usuario ya existe.
     */
    @Transactional
    public User createUser(UserDTO userDTO) {
        if (isUsernameTaken(userDTO.getName())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + userDTO.getName());
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Método para encontrar un usuario por su nombre de usuario.
     *
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, de lo contrario está vacío.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    /**
     * Método para verificar las credenciales de inicio de sesión.
     *
     * @param username   El nombre de usuario.
     * @param rawPassword La contraseña sin cifrar.
     * @return true si las credenciales son correctas, de lo contrario false.
     */
    public boolean checkCredentials(String username, String rawPassword) {
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return PasswordUtil.checkPassword(rawPassword, user.getPassword());
        }
        return false;
    }
}

