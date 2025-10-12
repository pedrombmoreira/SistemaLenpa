package com.projeto.sistema_lenpa.model.lote_mudas;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class LoteMudasDTO {

    @NotNull(message = "É obrigatório selecionar uma planta")
    private Integer idPlanta;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1")
    private int quantidade;

    @PastOrPresent(message = "A data de entrada não pode ser uma data futura")
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
