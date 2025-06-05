package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.comprador.Comprador;
import com.projeto.sistema_lenpa.model.comprador.CompradorDTO;
import com.projeto.sistema_lenpa.model.comprador.CompradorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compradores")
public class CompradorController {

    @Autowired
    private CompradorRepository compradorRepository;


    @GetMapping("/listar")
    public String getCompradores(Model model) {
        var compradores = compradorRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

        //Testa se o CPF já foi cadastrado no banco
        if (compradorRepository.findByCpf(compradorDTO.getCpf()) != null) {
            result.addError(
                    new FieldError("compradorDTO", "cpf", compradorDTO.getCpf()
                            , false, null, null, "CPF já cadastrado")
            );
        }

        if (result.hasErrors()) {
            return "compradores/cadastroComprador";
        }

        Comprador comprador = compradorDTO.toEntity();
        compradorRepository.save(comprador);

        return "redirect:/compradores/listar";
    }

    @GetMapping("/editar")
    public String editarComprador(Model model, @RequestParam int id) {

        Comprador comprador = compradorRepository.findById(id).orElse(null);
        if (comprador == null) {
            return "redirect:/compradores/listar";
        }

        CompradorDTO compradorDTO = CompradorDTO.fromEntity(comprador);
        model.addAttribute("compradorDTO", compradorDTO);

        return "compradores/editarComprador";
    }

    @PostMapping("/editar")
    public String editarComprador(Model model,
                                @RequestParam int id,
                                @Valid @ModelAttribute CompradorDTO compradorDTO,
                                BindingResult result) {

        Comprador comprador = compradorRepository.findById(id).orElse(null);
        if (comprador == null) {
            return "redirect:/compradores/listar";
        }

        model.addAttribute("comprador", comprador);

        // Verifica se o cpf que está sendo alterado e se já existe
        if (!comprador.getCpf().equals(compradorDTO.getCpf()) &&
                compradorRepository.findByCpf(compradorDTO.getCpf()) != null) {
            result.addError(new FieldError("compradorDTO", "cpf", "CPF já cadastrado"));
        }

        if (result.hasErrors()) {
            return "compradores/editarComprador";
        }

        compradorDTO.updateEntity(comprador);
        compradorRepository.save(comprador);

        return "redirect:/compradores/listar";
    }

    @GetMapping("/deletar")
    public String deletarComprador(@RequestParam int id) {

        Comprador comprador = compradorRepository.findById(id).orElse(null);
        if (comprador != null) {
            compradorRepository.delete(comprador);
        }

        return "redirect:/compradores/listar";
    }

}
