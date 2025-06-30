package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.service.PlantaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/plantas")
public class    PlantaController {

    @Autowired
    private PlantaService plantaService;

    @GetMapping("/listar")
    public String getPlantas(Model model) {
        List<Planta> plantas = plantaService.getPlantas();
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

        plantaService.cadastrarPlanta(plantaDTO);

        return "redirect:/plantas/listar";
    }

    @GetMapping("/editar")
    public String editarPlanta(Model model, @RequestParam int id) {

        try {
            PlantaDTO plantaDTO = plantaService.buscaPlantaEdicao(id);
            model.addAttribute("plantaDTO", plantaDTO);
            model.addAttribute("id", id);
            return "plantas/editarPlanta";

        } catch (IllegalArgumentException e) {
            System.err.println("GET /editar: " + e.getMessage());
            return "redirect:/plantas/listar";
        }
    }

    @PostMapping("/editar")
    public String editarPlanta(Model model,
                                @RequestParam int id,
                                @Valid @ModelAttribute PlantaDTO plantaDTO,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "plantas/editarPlanta";
        }

        plantaService.editarPlanta(id, plantaDTO);

        return "redirect:/plantas/listar";
    }

    @PostMapping("/deletar")
    public String deletarPlanta(@RequestParam int id) {

        try {
            // Tenta executar a exclusão através do serviço
            plantaService.deletarPlanta(id);

        } catch (IllegalArgumentException e) {
            System.err.println("Tentativa de deletar planta inexistente. ID: " + id + ". Mensagem: " + e.getMessage());
        }
        return "redirect:/plantas/listar";
    }

}
