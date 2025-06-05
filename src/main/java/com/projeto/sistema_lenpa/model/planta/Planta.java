package com.projeto.sistema_lenpa.model.planta;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="plantas")
@NoArgsConstructor
@AllArgsConstructor
public class Planta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome_popular;
    private String nome_cientifico;
    private String tipo;
    @Column(length = 1000)
    private String descricao_manejo;
    private String foto_url;
    private int quantidade_mudas;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome_popular() {
        return nome_popular;
    }

    public void setNome_popular(String nome_popular) {
        this.nome_popular = nome_popular;
    }

    public String getNome_cientifico() {
        return nome_cientifico;
    }

    public void setNome_cientifico(String nome_cientifico) {
        this.nome_cientifico = nome_cientifico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao_manejo() {
        return descricao_manejo;
    }

    public void setDescricao_manejo(String descricao_manejo) {
        this.descricao_manejo = descricao_manejo;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public int getQuantidade_mudas() {
        return quantidade_mudas;
    }

    public void setQuantidade_mudas(int quantidade_mudas) {
        this.quantidade_mudas = quantidade_mudas;
    }
}
