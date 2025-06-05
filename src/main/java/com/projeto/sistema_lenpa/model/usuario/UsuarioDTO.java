package com.projeto.sistema_lenpa.model.usuario;

import org.hibernate.validator.constraints.NotEmpty;

public class UsuarioDTO {

    @NotEmpty(message = "Preencha o nome")
    private String nome;
    @NotEmpty(message = "Preencha o login")
    private String login;
    @NotEmpty(message = "Preencha a senha")
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setPermissao(Usuario.Permissao.TECNICO);
        return usuario;
    }

    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setLogin(usuario.getLogin());
        usuarioDTO.setSenha(usuario.getSenha());
        return usuarioDTO;
    }

    public void updateEntity(Usuario usuario) {
        usuario.setNome(this.nome);
        usuario.setLogin(this.login);
        usuario.setSenha(this.senha);
    }

}
