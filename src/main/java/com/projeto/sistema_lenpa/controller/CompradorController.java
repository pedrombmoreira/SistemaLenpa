package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.comprador.CompradorDTO;
import com.projeto.sistema_lenpa.service.CompradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/compradores")
public class CompradorController {

    @Autowired
    private CompradorService compradorService;


    @GetMapping("/listar")
    public String getCompradores(Model model) {
        List<Comprador> compradores = compradorService.getCompradores();
        model.addAttribute("compradores", compradores);
        return "compradores/listaComprador";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        CompradorDTO compradorDTO = new CompradorDTO();
        model.addAttribute("compradorDTO", compradorDTO);
        return "compradores/cadastroComprador";
    }

    @PostMapping("/cadastrar")
    public String cadastrarComprador(@Valid @ModelAttribute CompradorDTO compradorDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "compradores/cadastroComprador";
        }

        try {
            compradorService.cadastrarComprador(compradorDTO);

        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("compradorDTO", "cpf", e.getMessage()));
            return "compradores/cadastroComprador";
        }

        return "redirect:/compradores/listar";
    }

    @GetMapping("/editar")
    public String editarComprador(Model model, @RequestParam int id) {

        try {
            CompradorDTO compradorDTO = compradorService.buscaCompradorEdicao(id);
            model.addAttribute("compradorDTO", compradorDTO);
            model.addAttribute("id", id);
            return "compradores/editarComprador";

        } catch (IllegalArgumentException e) {
            System.err.println("GET /editar: " + e.getMessage());
            return "redirect:/compradores/listar";
        }
    }

    @PostMapping("/editar")
    public String editarComprador(
            @RequestParam int id,
            @Valid @ModelAttribute CompradorDTO compradorDTO,
            BindingResult result) {

        if (result.hasErrors()) {
            return "compradores/editarComprador";
        }

        try {
            compradorService.editarComprador(id, compradorDTO);

        } catch (IllegalArgumentException e) {
            System.err.println("POST /editar: " + e.getMessage());

            // Verificamos o conteúdo da mensagem para decidir o que fazer.
            // Se for um erro de "não encontrado", redirecionamos.
            if (e.getMessage().contains("não encontrado")) {
                return "redirect:/compradores/listar";
            } else {
                // Senão, é um erro de regra de negócio (CPF duplicado).
                // Adicionamos ao BindingResult e voltamos para o formulário.
                result.addError(new FieldError("compradorDTO", "cpf", e.getMessage()));
                return "compradores/editarComprador";
            }
        }

        return "redirect:/compradores/listar";
    }

    @PostMapping("/deletar")
    public String deletarComprador(@RequestParam int id) {

        try {
            compradorService.deletarComprador(id);
        } catch (IllegalArgumentException e) {
            System.err.println("Tentativa de deletar comprador inexistente. ID: " + id + ". Mensagem: " + e.getMessage());
        }

        return "redirect:/compradores/listar";
    }

}
