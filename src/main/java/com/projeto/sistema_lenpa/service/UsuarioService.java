package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.model.usuario.UsuarioDTO;
import com.projeto.sistema_lenpa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void cadastrarUsuario(UsuarioDTO usuarioDTO) {

        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()) != null) {

            throw new IllegalArgumentException("O e-mail '" + usuarioDTO.getEmail() + "' já está em uso.");

        }

        Usuario usuario = usuarioDTO.toEntity();
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuarioRepository.save(usuario);
    }

    public UsuarioDTO buscaUsuarioEdicao(int id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + id + " não encontrado."));

        return UsuarioDTO.fromEntity(usuario);
    }

    public void editarUsuario(int id, UsuarioDTO usuarioDTO) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao atualizar: Usuário com ID " + id + " não encontrado."));

        // Validação de e-mail: verifica se o e-mail está sendo alterado E se o novo e-mail já pertence a OUTRO usuário.
        if (!usuario.getEmail().equals(usuarioDTO.getEmail())) {
            Usuario usuarioComNovoEmail = usuarioRepository.findByEmail(usuarioDTO.getEmail());
            if (usuarioComNovoEmail != null && usuarioComNovoEmail.getId() != usuario.getId()) {
                throw new IllegalArgumentException("O e-mail '" + usuarioDTO.getEmail() + "' já está em uso por outro usuário.");
            }
        }

        usuarioDTO.updateEntity(usuario);
        usuarioDTO.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);

    }

    public void deletarUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Falha ao deletar: Usuário com ID " + id + " não encontrado."));
        usuarioRepository.delete(usuario);
    }

}
