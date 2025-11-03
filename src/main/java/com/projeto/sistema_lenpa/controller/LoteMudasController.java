package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.lote_mudas.LoteMudasDTO;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import com.projeto.sistema_lenpa.service.LoteMudasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lotes")
public class LoteMudasController {

    @Autowired
    private LoteMudasService loteMudasService;

    @Autowired
    private PlantaRepository plantaRepository;

    @GetMapping("/listar")
    public String listarLotes(Model model) {
        model.addAttribute("lotes", loteMudasService.getLotes());
        return "lotes/listaLote";
    }

    @GetMapping("/cadastrar")
    public String cadastrarLoteForm(Model model) {
        model.addAttribute("loteMudasDTO", new LoteMudasDTO());
        model.addAttribute("plantas", plantaRepository.findAll());
        return "lotes/cadastroLote";
    }

    @PostMapping("/cadastrar")
    public String cadastrarLote(@Valid @ModelAttribute LoteMudasDTO loteMudasDTO,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("plantas", plantaRepository.findAll());
            return "lotes/cadastroLote";
        }

        try {
            loteMudasService.registrarNovoLote(loteMudasDTO);
        } catch (IllegalArgumentException e) {
            result.rejectValue("plantaId", "error.loteMudasDTO", e.getMessage());
            model.addAttribute("plantas", plantaRepository.findAll());
            return "lotes/cadastroLote";
        }

        return "redirect:/lotes/listar";
    }

    @PostMapping("/deletar")
    public String deletarLote(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            loteMudasService.deletarLote(id);
        } catch (IllegalArgumentException | IllegalStateException e) {
            String mensagemErro = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", mensagemErro);
        }
        return "redirect:/lotes/listar";
    }

}
