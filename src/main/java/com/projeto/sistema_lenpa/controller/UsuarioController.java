package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.usuario.Usuario;
import com.projeto.sistema_lenpa.model.usuario.UsuarioDTO;
import com.projeto.sistema_lenpa.model.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/listar")
    public String getUsuarios(Model model) {
        var usuarios = usuarioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

        //Testa se o login já foi cadastrado no banco
        if (usuarioRepository.findByLogin(usuarioDTO.getLogin()) != null) {
            result.addError(
                    new FieldError("usuarioDTO", "login", usuarioDTO.getLogin()
                    , false, null, null, "Login já em uso")
            );
        }

        //Testa se a senha já foi cadastrada
        if (usuarioRepository.findBySenha(usuarioDTO.getSenha()) != null) {
            result.addError(
                    new FieldError("usuarioDTO", "senha", usuarioDTO.getSenha()
                            , false, null, null, "Senha já em uso")
            );
        }

        if (result.hasErrors()) {
            return "usuarios/cadastroUsuario";
        }

        Usuario usuario = usuarioDTO.toEntity();
        usuarioRepository.save(usuario);

        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar")
    public String editarCliente(Model model, @RequestParam int id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios/listar";
        }

        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);

        return "usuarios/editarUsuario";
    }

    @PostMapping("/editar")
    public String editarUsuario(Model model,
                                @RequestParam int id,
                                @Valid @ModelAttribute UsuarioDTO usuarioDTO,
                                BindingResult result) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios/listar";
        }

        model.addAttribute("usuario", usuario);

        // Verifica se o login está sendo alterado e se já existe
        if (!usuario.getLogin().equals(usuarioDTO.getLogin()) &&
                usuarioRepository.findByLogin(usuarioDTO.getLogin()) != null) {
            result.addError(new FieldError("usuarioDTO", "login", "Login já em uso"));
        }

        // Verifica se a senha está sendo alterada e se já existe
        if (!usuario.getSenha().equals(usuarioDTO.getSenha()) &&
                usuarioRepository.findBySenha(usuarioDTO.getSenha()) != null) {
            result.addError(new FieldError("usuarioDTO", "senha", "Senha já em uso"));
        }

        if (result.hasErrors()) {
            return "usuarios/editarUsuario";
        }

        usuarioDTO.updateEntity(usuario);
        usuarioRepository.save(usuario);

        return "redirect:/usuarios/listar";
    }

    @GetMapping("/deletar")
    public String deletarUsuario(@RequestParam int id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }

    return "redirect:/usuarios/listar";
    }

}
