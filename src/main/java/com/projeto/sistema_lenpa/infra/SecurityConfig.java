package com.projeto.sistema_lenpa.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // Ignora a proteção CSRF para todas as rotas de API
                )
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Permite acesso público a estas rotas
                        .requestMatchers("/", "/login", "/index", "/css/**", "/js/**", "/uploads/**", "/images/**", "/api/plantas/**", "/planta/**").permitAll()

                        // 2. Apenas quem tem o papel 'GESTOR' pode acessar as rotas de usuários
                        .requestMatchers("/usuarios/**").hasRole("GESTOR")

                        // 3. Quem tem papel 'GESTOR' OU 'TECNICO' pode acessar as outras telas de gestão
                        .requestMatchers("/plantas/**", "/lotes/**", "/entregas/**", "/compradores/**").hasAnyRole("GESTOR", "TECNICO")

                        // 4. Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 5. Define a página de login customizada
                        .loginPage("/login")
                        .permitAll() // Todos podem acessar a página de login
                        .defaultSuccessUrl("/", true) // Redireciona para a home após o login com sucesso
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logout") // Redireciona para a home com um parâmetro de logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
