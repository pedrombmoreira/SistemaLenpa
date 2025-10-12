package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import com.projeto.sistema_lenpa.service.PlantaService; // Importe o serviço
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/plantas")
@CrossOrigin(origins = "*")
public class PlantaApiController {

    // 1. Injetamos o SERVIÇO, que contém nossa lógica de negócio.
    @Autowired
    private PlantaService plantaService;
    @Autowired
    private PlantaRepository plantaRepository;

    // O método de listar agora também usa o serviço, para manter o padrão.
    @GetMapping
    public List<Planta> listarPlantas() {
        return plantaService.getPlantas(); // Supondo que este método exista no seu serviço
    }

    // O método de buscar por ID também usa o serviço.
    @GetMapping("/{id}")
    public ResponseEntity<Planta> getPlantaById(@PathVariable Integer id) {
        try {
            Planta planta = plantaRepository.findById(id).get();
            return ResponseEntity.ok(planta);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 2. ESTE É O NOVO MÉTODO DE CADASTRO, totalmente modificado.
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> criarPlanta(
            // 3. Recebemos cada campo de texto individualmente
            @RequestParam("nome_popular") String nomePopular,
            @RequestParam("nome_cientifico") String nomeCientifico,
            @RequestParam("tipo") String tipo,
            @RequestParam("descricao_manejo") String descricaoManejo,
            @RequestParam("quantidade") int quantidade,
            // 4. E recebemos o arquivo da imagem separadamente
            @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile
    ) {
        try {
            // 5. Montamos o DTO com os dados recebidos
            PlantaDTO dto = new PlantaDTO();
            dto.setNome_popular(nomePopular);
            dto.setNome_cientifico(nomeCientifico);
            dto.setTipo(tipo);
            dto.setDescricao_manejo(descricaoManejo);
            dto.setQuantidade_mudas(quantidade); // Garanta que seu DTO tenha o campo 'quantidade'

            // 6. Chamamos o serviço, que fará todo o trabalho pesado
            Planta plantaSalva = plantaService.cadastrarPlanta(dto, imagemFile);

            return new ResponseEntity<>(plantaSalva, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("Erro ao salvar a imagem.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao processar a requisição: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}