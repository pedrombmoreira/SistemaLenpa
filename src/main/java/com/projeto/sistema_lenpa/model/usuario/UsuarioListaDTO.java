package com.projeto.sistema_lenpa.model.usuario;

public class UsuarioListaDTO {

    private int id;
    private String nome;
    private String email;
    private Usuario.Permissao permissao;
    private boolean podeExcluir;

    public UsuarioListaDTO(Usuario usuario, boolean podeExcluir) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.permissao = usuario.getPermissao();
        this.podeExcluir = podeExcluir;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Usuario.Permissao getPermissao() { return permissao; }
    public boolean isPodeExcluir() { return podeExcluir; }

}
