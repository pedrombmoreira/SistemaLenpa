package com.projeto.sistema_lenpa.model.entrega;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.usuario.Usuario;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EntregaDTO {

    private Integer idComprador;
    private LocalDate dataEntrega;
    private int quantidade_mudas;
    private BigDecimal valor;
    private String destino;
    private Integer idUsuario;
    private Integer idPlanta;

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public Integer getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Integer idComprador) {
        this.idComprador = idComprador;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
    }

    public int getQuantidade_mudas() {
        return quantidade_mudas;
    }

    public void setQuantidade_mudas(int quantidade_mudas) {
        this.quantidade_mudas = quantidade_mudas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }


    public Entrega toEntity(Comprador comprador, Usuario usuario, Planta planta) {
        Entrega entrega = new Entrega();
        entrega.setComprador(comprador);
        entrega.setDataEntrega(LocalDate.now());
        entrega.setQuantidade_mudas(quantidade_mudas);
        entrega.setValor(valor);
        entrega.setDestino(destino);
        entrega.setUsuario(usuario);
        entrega.setPlanta(planta);
        return entrega;
    }

    public static EntregaDTO fromEntity(Entrega entrega) {
        EntregaDTO dto = new EntregaDTO();
        dto.setIdComprador(entrega.getComprador().getId());
        dto.setDataEntrega(LocalDate.now());
        dto.setQuantidade_mudas(entrega.getQuantidade_mudas());
        dto.setValor(entrega.getValor());
        dto.setDestino(entrega.getDestino());
        dto.setIdUsuario(entrega.getUsuario().getId());
        dto.setIdPlanta(entrega.getPlanta().getId());
        return dto;
    }

    public void updateEntity(Entrega entrega, Comprador comprador, Usuario usuario, Planta planta) {
        entrega.setComprador(comprador);
        entrega.setQuantidade_mudas(this.quantidade_mudas);
        entrega.setValor(this.valor);
        entrega.setDestino(this.destino);
        entrega.setUsuario(usuario);
        entrega.setPlanta(planta);
    }
}
