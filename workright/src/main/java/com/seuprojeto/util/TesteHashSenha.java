package com.seuprojeto.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteHashSenha {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashDoBanco = "$2a$10$TmsHc5nBM3C7wqqwNWwt9uh0KFNw/M2WwL0NhMzHWJlE/HM7bDZHa"; 
        
        
        String senhaTeste = "123456";

        boolean matches = encoder.matches(senhaTeste, hashDoBanco);
        System.out.println("Senha bate com hash do banco? " + matches);
    }
}
