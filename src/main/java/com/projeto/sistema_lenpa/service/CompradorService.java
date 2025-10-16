package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.comprador.CompradorDTO;
import com.projeto.sistema_lenpa.model.comprador.CompradorListaDTO;
import com.projeto.sistema_lenpa.repository.CompradorRepository;
import com.projeto.sistema_lenpa.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompradorService {

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    public List<CompradorListaDTO> getCompradores() {

        List<Comprador> compradores = compradorRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<CompradorListaDTO> dtosParaRetornar = new ArrayList<>();

        //Percorre a lista original com um laço for
        for (Comprador comprador : compradores) {
            //Para cada comprador, faz a verificação de vínculos
            boolean temVinculos = entregaRepository.existsByCompradorId(comprador.getId());

            //Cria o novo DTO com a informação de exclusão
            CompradorListaDTO dto = new CompradorListaDTO(comprador, !temVinculos);

            //Adiciona o DTO recém-criado na nossa lista de retorno
            dtosParaRetornar.add(dto);
        }

        return dtosParaRetornar;
    }

    public void cadastrarComprador(CompradorDTO compradorDTO) {

        if (compradorRepository.findByCpf(compradorDTO.getCpf()) != null) {
            throw new IllegalArgumentException("O CPF '" + compradorDTO.getCpf() + "' já está em uso.");
        }

        Comprador comprador = compradorDTO.toEntity();

        compradorRepository.save(comprador);
    }

    public CompradorDTO buscaCompradorEdicao(int id) {

        Comprador comprador = compradorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comprador com ID " + id + " não encontrado."));

        return CompradorDTO.fromEntity(comprador);
    }

    public void editarComprador(int id, CompradorDTO compradorDTO) {

        Comprador comprador = compradorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao atualizar: Comprador com ID " + id + " não encontrado."));

        if (!comprador.getCpf().equals(compradorDTO.getCpf())) {
            Comprador compradorComNovoCPF = compradorRepository.findByCpf(compradorDTO.getCpf());
            if (compradorComNovoCPF != null && compradorComNovoCPF.getId() != comprador.getId()) {
                throw new IllegalArgumentException("O CPF '" + compradorDTO.getCpf() + "' já está em uso por outro comprador.");
            }
        }

        compradorDTO.updateEntity(comprador);
        compradorRepository.save(comprador);

    }

    public void deletarComprador(int id) {
        Comprador comprador = compradorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Falha ao deletar: Comprador com ID " + id + " não encontrado."));
        compradorRepository.delete(comprador);
    }
}
