package com.projeto.sistema_lenpa.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaPura = "1234";
        String senhaCriptografada = encoder.encode(senhaPura);
        System.out.println("Sua senha criptografada é: " + senhaCriptografada);
    }
}
