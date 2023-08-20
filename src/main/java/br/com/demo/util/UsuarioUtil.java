package br.com.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UsuarioUtil {

    private UsuarioUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String hashSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

}
