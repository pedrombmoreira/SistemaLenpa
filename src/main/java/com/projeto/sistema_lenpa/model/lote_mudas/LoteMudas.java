package com.projeto.sistema_lenpa.model.lote_mudas;


import com.projeto.sistema_lenpa.model.planta.Planta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name="lote_mudas")
@NoArgsConstructor
@AllArgsConstructor
public class LoteMudas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "planta_id")
    private Planta planta;
    private LocalDate data_geracao;
    private int quantidade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public LocalDate getData_geracao() {
        return data_geracao;
    }

    public void setData_geracao(LocalDate data_geracao) {
        this.data_geracao = data_geracao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
