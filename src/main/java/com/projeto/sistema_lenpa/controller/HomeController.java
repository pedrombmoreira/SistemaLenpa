package com.projeto.sistema_lenpa.controller;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.service.PlantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PlantaService plantaService;

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value = "busca", required = false) String termoBusca,
                       @RequestParam(value = "tipo", required = false) String tipoPlanta) {

        List<Planta> plantas = plantaService.buscarPlantasPublico(termoBusca, tipoPlanta);
        model.addAttribute("plantas", plantas);

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Retorna o nome do template login.html
    }
}
