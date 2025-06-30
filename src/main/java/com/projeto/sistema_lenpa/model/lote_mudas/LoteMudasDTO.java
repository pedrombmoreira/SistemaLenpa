package com.projeto.sistema_lenpa.model.lote_mudas;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class LoteMudasDTO {

    @NotNull
    private Integer idPlanta;
    @Min(1)
    private int quantidade;
    private LocalDate data_geracao;

    public Integer getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getData_geracao() {
        return data_geracao;
    }

    public void setData_geracao(LocalDate data_geracao) {
        this.data_geracao = data_geracao;
    }

}
