package com.spring.productsshop.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    // Instancia de BCryptPasswordEncoder para encriptar y verificar contrase�as
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // M�todo para encriptar una contrase�a
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password); // Devuelve la contrase�a encriptada
    }

    // M�todo para verificar si una contrase�a sin encriptar coincide con una contrase�a encriptada
    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword); // Devuelve true si las contrase�as coinciden, false en caso contrario
    }
}

