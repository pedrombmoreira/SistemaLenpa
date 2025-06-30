package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.model.usuario.UsuarioDTO;
import com.projeto.sistema_lenpa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listar")
    public String getUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.getUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listaUsuario";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        model.addAttribute("usuarioDTO", usuarioDTO);
        return "usuarios/cadastroUsuario";
    }

    @PostMapping("/cadastrar")
    public String cadastrarUsuario(@Valid @ModelAttribute UsuarioDTO usuarioDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "usuarios/cadastroUsuario";
        }

        try {
            usuarioService.cadastrarUsuario(usuarioDTO);

        } catch (IllegalArgumentException e) {
            result.addError(new FieldError("usuarioDTO", "email", e.getMessage()));
            return "usuarios/cadastroUsuario";
        }

        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar")
    public String editarUsuario(Model model, @RequestParam int id) {

        try {
            UsuarioDTO usuarioDTO = usuarioService.buscaUsuarioEdicao(id);
            model.addAttribute("usuarioDTO", usuarioDTO);
            model.addAttribute("id", id);
            return "usuarios/editarUsuario";

        } catch (IllegalArgumentException e) {
            System.err.println("GET /editar: " + e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    @PostMapping("/editar")
    public String editarUsuario(
                                @RequestParam int id,
                                @Valid @ModelAttribute UsuarioDTO usuarioDTO,
                                BindingResult result) {

        if (result.hasErrors()) {
            return "usuarios/editarUsuario";
        }

        try {
            usuarioService.editarUsuario(id, usuarioDTO);

        } catch (IllegalArgumentException e) {
            System.err.println("POST /editar: " + e.getMessage());

            if (e.getMessage().contains("não encontrado")) {
                return "redirect:/usuarios/listar";
            } else {
                // Senão, é um erro de regra de negócio (email duplicado).
                // Adicionamos ao BindingResult e voltamos para o formulário.
                result.addError(new FieldError("usuarioDTO", "email", e.getMessage()));
                return "usuarios/editarUsuario";
            }
        }

        return "redirect:/usuarios/listar";
    }

    @PostMapping("/deletar")
    public String deletarUsuario(@RequestParam int id) {

        try {
            usuarioService.deletarUsuario(id);
        } catch (IllegalArgumentException e) {
            System.err.println("Tentativa de deletar usuário inexistente. ID: " + id + ". Mensagem: " + e.getMessage());
        }

        return "redirect:/usuarios/listar";
    }

}
