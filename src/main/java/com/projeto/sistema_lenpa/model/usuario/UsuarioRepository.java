package com.projeto.sistema_lenpa.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByLogin(String login);
    public Usuario findBySenha(String senha);
}
