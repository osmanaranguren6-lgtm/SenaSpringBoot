package com.sena.controller;

import com.sena.service.AprendizService;
import com.sena.service.ProgramaService;
import com.sena.service.FichaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de inicio
 */
@Controller
public class HomeController {

    private final AprendizService aprendizService;
    private final ProgramaService programaService;
    private final FichaService fichaService;

    public HomeController(AprendizService aprendizService, ProgramaService programaService, FichaService fichaService) {
        this.aprendizService = aprendizService;
        this.programaService = programaService;
        this.fichaService = fichaService;
    }

    @GetMapping({"", "/", "/home", "/index"})
    public String home(Model model) {
        model.addAttribute("totalAprendices", aprendizService.getTotalAprendices());
        model.addAttribute("totalProgramas", programaService.getTotalProgramas());
        model.addAttribute("totalFichas", fichaService.getTotalFichas());
        return "index";
    }
}
