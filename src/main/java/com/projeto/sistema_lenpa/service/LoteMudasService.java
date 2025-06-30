package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.lote_mudas.LoteMudas;
import com.projeto.sistema_lenpa.model.lote_mudas.LoteMudasDTO;
import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.repository.LoteMudasRepository;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoteMudasService {

    @Autowired
    private LoteMudasRepository loteMudasRepository;

    @Autowired
    private PlantaRepository plantaRepository;

    public List<LoteMudas> getLotes() {
        return loteMudasRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public void registrarNovoLote(LoteMudasDTO loteMudasDTO) {
        Planta planta = plantaRepository.findById(loteMudasDTO.getIdPlanta())
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + loteMudasDTO.getIdPlanta() + " não encontrada."));

        LoteMudas novoLote = new LoteMudas();
        novoLote.setPlanta(planta);
        novoLote.setQuantidade(loteMudasDTO.getQuantidade());
        novoLote.setData_geracao(loteMudasDTO.getData_geracao() != null ? loteMudasDTO.getData_geracao() : LocalDate.now());

        //ATUALIZA o estoque na entidade Planta
        int estoqueAtualizado = planta.getQuantidade_mudas() + novoLote.getQuantidade();
        planta.setQuantidade_mudas(estoqueAtualizado);

        loteMudasRepository.save(novoLote);
        plantaRepository.save(planta);
    }

    public void deletarLote(Integer id) {
        LoteMudas lote = loteMudasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao deletar: Lote com ID " + id + " não encontrado."));

        Planta planta = lote.getPlanta();

        // Verificação de segurança: impede que o estoque fique negativo se algo estiver inconsistente.
        if (planta.getQuantidade_mudas() < lote.getQuantidade()) {
            throw new IllegalStateException(
                    "Inconsistência de dados! Deletar o lote " + lote.getId() +
                            " deixaria o estoque da planta '" + planta.getNome_popular() + "' negativo."
            );
        }

        int estoqueAtualizado = planta.getQuantidade_mudas() - lote.getQuantidade();
        planta.setQuantidade_mudas(estoqueAtualizado);

        plantaRepository.save(planta);
        loteMudasRepository.delete(lote);
    }

}
