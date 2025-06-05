package com.projeto.sistema_lenpa.model.comprador;

import jakarta.validation.constraints.NotEmpty;

public class CompradorDTO {

    @NotEmpty(message = "Preencha o nome")
    private String nome;
    @NotEmpty(message = "Preencha o CPF")
    private String cpf;
    @NotEmpty(message = "Preencha o telefone")
    private String telefone;
    private Boolean associado_cespol;

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


    public Comprador toEntity() {
        Comprador comprador = new Comprador();
        comprador.setNome(nome);
        comprador.setCpf(cpf);
        comprador.setTelefone(telefone);
        comprador.setAssociado_cespol(associado_cespol);
        return comprador;
    }

    public static CompradorDTO fromEntity(Comprador comprador) {
        CompradorDTO compradorDTO = new CompradorDTO();
        compradorDTO.setNome(comprador.getNome());
        compradorDTO.setCpf(comprador.getCpf());
        compradorDTO.setTelefone(comprador.getTelefone());
        compradorDTO.setAssociado_cespol(comprador.getAssociado_cespol());
        return compradorDTO;
    }

    public void updateEntity(Comprador comprador) {
        comprador.setNome(this.nome);
        comprador.setCpf(this.cpf);
        comprador.setTelefone(this.telefone);
        comprador.setAssociado_cespol(this.associado_cespol);
    }
}
