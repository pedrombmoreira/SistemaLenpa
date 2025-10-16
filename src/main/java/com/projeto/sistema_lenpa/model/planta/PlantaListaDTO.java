package com.projeto.sistema_lenpa.model.planta;

public class PlantaListaDTO {

    private Integer id;
    private String nome_popular;
    private String nome_cientifico;
    private String tipo;
    private String foto_url;
    private int quantidade_mudas;
    private String descricao_manejo;
    private boolean podeExcluir;

    public PlantaListaDTO(Planta planta, boolean podeExcluir) {
        this.id = planta.getId();
        this.nome_popular = planta.getNome_popular();
        this.nome_cientifico = planta.getNome_cientifico();
        this.tipo = planta.getTipo();
        this.foto_url = planta.getFoto_url();
        this.quantidade_mudas = planta.getQuantidade_mudas();
        this.descricao_manejo = planta.getDescricao_manejo();
        this.podeExcluir = podeExcluir;
    }

    public Integer getId() { return id; }
    public String getNome_popular() { return nome_popular; }
    public String getNome_cientifico() { return nome_cientifico; }
    public String getTipo() { return tipo; }
    public String getFoto_url() { return foto_url; }
    public int getQuantidade_mudas() { return quantidade_mudas; }
    public String getDescricao_manejo() { return descricao_manejo; }
    public boolean isPodeExcluir() { return podeExcluir; }
}
