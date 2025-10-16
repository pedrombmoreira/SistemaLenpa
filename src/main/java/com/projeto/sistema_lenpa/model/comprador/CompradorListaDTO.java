package com.projeto.sistema_lenpa.model.comprador;

public class CompradorListaDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;
    private Boolean associado_cespol;
    private boolean podeExcluir;

    public CompradorListaDTO(Comprador comprador, boolean podeExcluir) {
        this.id = comprador.getId();
        this.nome = comprador.getNome();
        this.cpf = comprador.getCpf();
        this.telefone = comprador.getTelefone();
        this.associado_cespol = comprador.getAssociado_cespol();
        this.podeExcluir = podeExcluir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAssociado_cespol() {
        return associado_cespol;
    }

    public void setAssociado_cespol(Boolean associado_cespol) {
        this.associado_cespol = associado_cespol;
    }

    public boolean isPodeExcluir() {
        return podeExcluir;
    }

    public void setPodeExcluir(boolean podeExcluir) {
        this.podeExcluir = podeExcluir;
    }
}

