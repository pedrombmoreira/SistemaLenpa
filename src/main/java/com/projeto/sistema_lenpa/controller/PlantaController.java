package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.model.planta.PlantaRepository;
import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.model.usuario.UsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/plantas")
public class PlantaController {

    @Autowired
    private PlantaRepository plantaRepository;

    @GetMapping("/listar")
    public String getPlantas(Model model) {
        var plantas = plantaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("plantas", plantas);
        return "plantas/listaPlanta";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        PlantaDTO plantaDTO = new PlantaDTO();
        model.addAttribute("plantaDTO", plantaDTO);
        return "plantas/cadastroPlanta";
    }

    @PostMapping("/cadastrar")
    public String cadastrarPlanta(@Valid @ModelAttribute PlantaDTO plantaDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "plantas/cadastroPlanta";
        }

        Planta planta = plantaDTO.toEntity();
        plantaRepository.save(planta);

        return "redirect:/plantas/listar";
    }

    @GetMapping("/editar")
    public String editarPlanta(Model model, @RequestParam int id) {

        Planta planta = plantaRepository.findById(id).orElse(null);

        if (planta == null) {
            return "redirect:/plantas/listar";
        }

        PlantaDTO plantaDTO = PlantaDTO.fromEntity(planta);
        model.addAttribute("plantaDTO", plantaDTO);

        return "plantas/editarPlanta";
    }

    @PostMapping("/editar")
    public String editarPlanta(Model model,
                                @RequestParam int id,
                                @Valid @ModelAttribute PlantaDTO plantaDTO,
                                BindingResult result) {

        Planta planta = plantaRepository.findById(id).orElse(null);

        if (planta == null) {
            return "redirect:/plantas/listar";
        }

        model.addAttribute("planta", planta);

        if (result.hasErrors()) {
            return "usuarios/editarUsuario";
        }

        plantaDTO.updateEntity(planta);
        plantaRepository.save(planta);

        return "redirect:/plantas/listar";
    }

    @GetMapping("/deletar")
    public String deletarPlanta(@RequestParam int id) {

        Planta planta = plantaRepository.findById(id).orElse(null);
        if (planta != null) {
            plantaRepository.delete(planta);
        }

        return "redirect:/plantas/listar";
    }

}
