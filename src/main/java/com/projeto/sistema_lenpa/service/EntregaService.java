package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.entrega.Entrega;
import com.projeto.sistema_lenpa.model.entrega.EntregaDTO;
import com.projeto.sistema_lenpa.model.entrega.RelatorioEntregasDTO;
import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.repository.CompradorRepository;
import com.projeto.sistema_lenpa.repository.EntregaRepository;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import com.projeto.sistema_lenpa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private PlantaRepository plantaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CompradorRepository compradorRepository;

    public List<Entrega> getEntregas() {
        return entregaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public void cadastrarEntrega(EntregaDTO entregaDTO, String emailUsuarioLogado) {

        Comprador comprador = compradorRepository.findById(entregaDTO.getIdComprador())
                .orElseThrow(() -> new IllegalArgumentException("Comprador com ID " + entregaDTO.getIdComprador() + " não encontrado."));

        Planta planta = plantaRepository.findById(entregaDTO.getIdPlanta())
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + entregaDTO.getIdPlanta() + " não encontrada."));

        if (planta.getQuantidade_mudas() < entregaDTO.getQuantidade_mudas()) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente para a planta '" + planta.getNome_popular() + "'. " +
                            "Estoque atual: " + planta.getQuantidade_mudas() + ", Quantidade solicitada: " + entregaDTO.getQuantidade_mudas()
            );
        }

        // Busca o usuário logado pelo email
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado);
        if (usuario == null) {
            throw new IllegalStateException("Usuário logado não encontrado no sistema.");
        }

        // Calcula o novo estoque
        int novoEstoque = planta.getQuantidade_mudas() - entregaDTO.getQuantidade_mudas();
        // Atualiza a quantidade na entidade Planta
        planta.setQuantidade_mudas(novoEstoque);

        Entrega novaEntrega = entregaDTO.toEntity(comprador, usuario, planta);

        //Salva a nova entrega E a planta com o estoque atualizado
        entregaRepository.save(novaEntrega);
        plantaRepository.save(planta); // Salva a planta com a quantidade atualizada

    }

    public EntregaDTO buscarEntregaEdicao(int id) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrega com ID " + id + " não encontrada."));
        return EntregaDTO.fromEntity(entrega);
    }

    public Entrega buscarEntidadePorId(int id) {
        return entregaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));
    }

    public RelatorioEntregasDTO buscarEntregasFiltradas(LocalDate dataInicio, LocalDate dataFim) {
        // Busca a lista de entregas já filtrada pelo repositório
        List<Entrega> entregasFiltradas = entregaRepository.findByFilter(dataInicio, dataFim);

        // Calcula o valor total apenas das VENDAS dentro da lista filtrada
        BigDecimal valorTotal = entregasFiltradas.stream()
                .map(entrega -> entrega.getValor() == null ? BigDecimal.ZERO : entrega.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma todos os valores

        // Retorna o DTO com a lista e o total calculado
        return new RelatorioEntregasDTO(entregasFiltradas, valorTotal);
    }

    public void atualizarEntrega(int id, EntregaDTO entregaDTO) {
        Entrega entregaExistente = entregaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao atualizar: Entrega com ID " + id + " não encontrada."));

        Comprador comprador = compradorRepository.findById(entregaDTO.getIdComprador())
                .orElseThrow(() -> new IllegalArgumentException("Comprador com ID " + entregaDTO.getIdComprador() + " não encontrado."));

        Planta planta = plantaRepository.findById(entregaDTO.getIdPlanta())
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + entregaDTO.getIdPlanta() + " não encontrada."));

        Usuario usuario = entregaExistente.getUsuario();

        entregaDTO.updateEntity(entregaExistente, comprador, usuario, planta);

        entregaRepository.save(entregaExistente);
    }

    public void deletarEntrega(int id) {
        if (!entregaRepository.existsById(id)) {
            throw new IllegalArgumentException("Falha ao deletar: Entrega com ID " + id + " não encontrada.");
        }
        entregaRepository.deleteById(id);
    }

}
