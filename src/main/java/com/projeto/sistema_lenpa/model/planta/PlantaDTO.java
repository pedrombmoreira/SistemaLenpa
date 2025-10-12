package com.projeto.sistema_lenpa.model.planta;
import org.hibernate.validator.constraints.NotEmpty;

public class PlantaDTO {

    @NotEmpty(message = "Preencha o nome popular")
    private String nome_popular;
    @NotEmpty(message = "Preencha o nome científico")
    private String nome_cientifico;
    @NotEmpty(message = "Selecione o tipo de planta")
    private String tipo;
    private String descricao_manejo; // opcional
    private String foto_url;
    private int quantidade_mudas;

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

    public Planta toEntity() {
        Planta planta = new Planta();
        planta.setNome_popular(nome_popular);
        planta.setNome_cientifico(nome_cientifico);
        planta.setTipo(tipo);
        planta.setDescricao_manejo(descricao_manejo);
        planta.setFoto_url(foto_url);
        planta.setQuantidade_mudas(quantidade_mudas);
        return planta;
    }

    public static PlantaDTO fromEntity(Planta planta) {
        PlantaDTO plantaDTO = new PlantaDTO();
        plantaDTO.setNome_popular(planta.getNome_popular());
        plantaDTO.setNome_cientifico(planta.getNome_cientifico());
        plantaDTO.setTipo(planta.getTipo());
        plantaDTO.setDescricao_manejo(planta.getDescricao_manejo());
        plantaDTO.setFoto_url(planta.getFoto_url());
        plantaDTO.setQuantidade_mudas(planta.getQuantidade_mudas());
        return plantaDTO;
    }

    public void updateEntity(Planta planta) {
        planta.setNome_popular(nome_popular);
        planta.setNome_cientifico(nome_cientifico);
        planta.setTipo(tipo);
        planta.setDescricao_manejo(descricao_manejo);
    }
}
