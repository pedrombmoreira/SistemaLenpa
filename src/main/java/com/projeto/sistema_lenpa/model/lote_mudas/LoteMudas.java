package com.projeto.sistema_lenpa.model.lote_mudas;


import com.projeto.sistema_lenpa.model.planta.Planta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name="lote_mudas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteMudas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "planta_id")
    private Planta planta;
    private Date data_geracao;
    private int quantidade;
}
