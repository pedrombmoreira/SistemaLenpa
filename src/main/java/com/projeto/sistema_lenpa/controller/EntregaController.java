package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.comprador.CompradorRepository;
import com.projeto.sistema_lenpa.model.entrega.Entrega;
import com.projeto.sistema_lenpa.model.entrega.EntregaDTO;
import com.projeto.sistema_lenpa.model.entrega.EntregaRepository;
import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.model.planta.PlantaRepository;
import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.model.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private CompradorRepository compradorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PlantaRepository plantaRepository;


    @GetMapping("/listar")
    public String getEntregas(Model model) {
        var entregas = entregaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("entregas", entregas);
        return "entregas/listaEntrega";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        EntregaDTO entregaDTO = new EntregaDTO();
        model.addAttribute("entregaDTO", entregaDTO);
        var compradores = compradorRepository.findAll();
        model.addAttribute("compradores", compradores);
        var usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        var plantas = plantaRepository.findAll();
        model.addAttribute("plantas", plantas);
        return "entregas/cadastroEntrega";
    }

    @PostMapping("/cadastrar")
    public String cadastrarEntrega(@Valid @ModelAttribute EntregaDTO entregaDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "entregas/cadastroEntrega";
        }

        Comprador comprador = compradorRepository.findById(entregaDTO.getIdComprador()).orElse(null);
        Usuario usuario = usuarioRepository.findById(entregaDTO.getIdUsuario()).orElse(null);
        Planta planta = plantaRepository.findById(entregaDTO.getIdPlanta()).orElse(null);

        Entrega entrega = entregaDTO.toEntity(comprador, usuario, planta);
        entregaRepository.save(entrega);

        return "redirect:/entregas/listar";
    }


    @GetMapping("/deletar")
    public String deletarEntrega(@RequestParam int id) {

        Entrega entrega = entregaRepository.findById(id).orElse(null);
        if (entrega != null) {
            entregaRepository.delete(entrega);
        }

        return "redirect:/entregas/listar";
    }
}
