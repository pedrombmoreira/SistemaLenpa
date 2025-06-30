package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usamos o email como "username" para o login
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email);
        }

        // Cria a lista de permissões (ROLES) do usuário
        Set<GrantedAuthority> authorities = new HashSet<>();
        // O Spring Security exige que as permissões comecem com "ROLE_"
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getPermissao().name()));

        // Retorna um objeto User do Spring Security
        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}
