package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.repository.CompradorRepository;
import com.projeto.sistema_lenpa.model.entrega.EntregaDTO;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import com.projeto.sistema_lenpa.repository.UsuarioRepository;
import com.projeto.sistema_lenpa.service.EntregaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @Autowired
    private CompradorRepository compradorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlantaRepository plantaRepository;


    @GetMapping("/listar")
    public String getEntregas(Model model) {
        model.addAttribute("entregas", entregaService.getEntregas());
        return "entregas/listaEntrega";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("entregaDTO", new EntregaDTO());
        model.addAttribute("compradores", compradorRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("plantas", plantaRepository.findAll());
        return "entregas/cadastroEntrega";
    }

    @PostMapping("/cadastrar")
    public String cadastrarEntrega(@Valid @ModelAttribute EntregaDTO entregaDTO, BindingResult result, Model model, Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("compradores", compradorRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("plantas", plantaRepository.findAll());
            return "entregas/cadastroEntrega";
        }

        try {
            entregaService.cadastrarEntrega(entregaDTO, principal.getName());
        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("entregaDTO", "idPlanta", e.getMessage()));
            result.addError(new FieldError("entregaDTO", "quantidade_mudas", e.getMessage()));

            model.addAttribute("compradores", compradorRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("plantas", plantaRepository.findAll());
            return "entregas/cadastroEntrega";
        }

        return "redirect:/entregas/listar";
    }

    @GetMapping("/editar")
    public String editarEntregaForm(Model model, @RequestParam int id) {
        try {
            EntregaDTO entregaDTO = entregaService.buscarEntregaEdicao(id);
            model.addAttribute("entregaDTO", entregaDTO);
            model.addAttribute("id", id);

            model.addAttribute("compradores", compradorRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("plantas", plantaRepository.findAll());

            return "entregas/editarEntrega";

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return "redirect:/entregas/listar";
        }
    }

    @PostMapping("/editar")
    public String processarEdicaoEntrega(@RequestParam int id,
                                         @Valid @ModelAttribute("entregaDTO") EntregaDTO entregaDTO,
                                         BindingResult result,
                                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("compradores", compradorRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("plantas", plantaRepository.findAll());
            return "entregas/editarEntrega";
        }

        try {
            entregaService.atualizarEntrega(id, entregaDTO);

        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("entregaDTO", "idPlanta", e.getMessage()));

            model.addAttribute("compradores", compradorRepository.findAll());
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("plantas", plantaRepository.findAll());
            return "entregas/editarEntrega";
        }

        return "redirect:/entregas/listar";
    }

    @PostMapping("/deletar")
    public String deletarEntrega(@RequestParam int id) {

        try {
            entregaService.deletarEntrega(id);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return "redirect:/entregas/listar";
    }
}
