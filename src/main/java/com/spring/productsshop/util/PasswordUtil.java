package com.spring.productsshop.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    // Instancia de BCryptPasswordEncoder para encriptar y verificar contraseñas
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para encriptar una contraseña
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password); // Devuelve la contraseña encriptada
    }

    // Método para verificar si una contraseña sin encriptar coincide con una contraseña encriptada
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword); // Devuelve true si las contraseñas coinciden, false en caso contrario
    }
}

