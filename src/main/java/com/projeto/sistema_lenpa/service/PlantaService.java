package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantaService {

    @Autowired
    private PlantaRepository plantaRepository;

    public List<Planta> getPlantas() {
        return plantaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public void cadastrarPlanta(PlantaDTO plantaDTO) {
        Planta planta = plantaDTO.toEntity();
        plantaRepository.save(planta);
    }

    public PlantaDTO buscaPlantaEdicao(int id) {

        Planta planta = plantaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + id + " não encontrada."));

        return PlantaDTO.fromEntity(planta);
    }

    public void editarPlanta(int id, PlantaDTO plantaDTO) {

        Planta planta = plantaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao atualizar: Planta com ID " + id + " não encontrada."));

        plantaDTO.updateEntity(planta);
        plantaRepository.save(planta);

    }

    public void deletarPlanta(int id) {
        Planta planta = plantaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Falha ao deletar: Planta com ID " + id + " não encontrado."));
        plantaRepository.delete(planta);
    }

    public List<Planta> buscarPlantasPublico(String termoBusca, String tipoPlanta) {
        return plantaRepository.search(termoBusca, tipoPlanta);
    }
}
