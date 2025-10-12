package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.entrega.Entrega;
import com.projeto.sistema_lenpa.model.entrega.RelatorioEntregasDTO;
import com.projeto.sistema_lenpa.repository.CompradorRepository;
import com.projeto.sistema_lenpa.model.entrega.EntregaDTO;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import com.projeto.sistema_lenpa.repository.UsuarioRepository;
import com.projeto.sistema_lenpa.service.EntregaService;
import com.projeto.sistema_lenpa.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

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
    @Autowired
    private PdfService pdfService;

    @GetMapping("/listar")
    public String getEntregas(Model model,
                              @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
                              @RequestParam(value = "dataFim", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim) {

        RelatorioEntregasDTO relatorio = entregaService.buscarEntregasFiltradas(dataInicio, dataFim);
        model.addAttribute("relatorio", relatorio);
        // Devolve os filtros para a view, para manter os campos preenchidos
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
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
            Entrega entrega = entregaService.buscarEntidadePorId(id);
            model.addAttribute("entrega", entrega);

            model.addAttribute("id", id);
            model.addAttribute("compradores", compradorRepository.findAll());
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

    @GetMapping("/exportar-pdf")
    public void exportarPdf(HttpServletResponse response,
                            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) throws IOException {

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=relatorio_entregas.pdf";
        response.setHeader(headerKey, headerValue);

        RelatorioEntregasDTO relatorio = entregaService.buscarEntregasFiltradas(dataInicio, dataFim);
        pdfService.gerarPdfRelatorioEntregas(response, relatorio);
    }
}
