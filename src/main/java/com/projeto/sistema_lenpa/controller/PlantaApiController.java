package com.projeto.sistema_lenpa.controller;


import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plantas")
@CrossOrigin(origins = "*")  // Habilita CORS para o Flutter acessar.
public class PlantaApiController {

    @Autowired
    private PlantaRepository plantaRepository;

    @GetMapping
    public List<Planta> listarPlantas() {
        return plantaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @PostMapping
    public ResponseEntity<Planta> criarPlanta(@RequestBody Planta planta) {
        try {
            Planta plantaSalva = plantaRepository.save(planta);
            return new ResponseEntity<>(plantaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
