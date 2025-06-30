package com.projeto.sistema_lenpa.repository;

import com.projeto.sistema_lenpa.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    public Usuario findByEmail(String email);
}
