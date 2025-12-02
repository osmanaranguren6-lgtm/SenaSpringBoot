package com.sena.controller;

import com.sena.exception.DuplicateResourceException;
import com.sena.exception.ResourceNotFoundException;
import com.sena.model.Ficha;
import com.sena.service.FichaService;
import com.sena.service.ProgramaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador web para gestión de Fichas
 */
@Controller
@RequestMapping("/fichas")
public class FichaController {

    private static final Logger log = LoggerFactory.getLogger(FichaController.class);

    private final FichaService fichaService;
    private final ProgramaService programaService;

    public FichaController(FichaService fichaService, ProgramaService programaService) {
        this.fichaService = fichaService;
        this.programaService = programaService;
    }

    /**
     * Lista de fichas
     */
    @GetMapping({"", "/", "/lista"})
    public String listarFichas(Model model) {
        log.info("Mostrando lista de fichas");
        List<Ficha> fichas = fichaService.getAllFichas();
        model.addAttribute("fichas", fichas);
        model.addAttribute("totalFichas", fichas.size());
        return "fichas/lista";
    }

    /**
     * Formulario para crear nueva ficha
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nueva ficha");
        model.addAttribute("ficha", new Ficha());
        model.addAttribute("programas", programaService.getAllProgramas());
        model.addAttribute("accion", "Crear");
        return "fichas/formulario";
    }

    /**
     * Procesar creación/actualización de ficha
     */
    @PostMapping("/guardar")
    public String guardarFicha(@Valid @ModelAttribute("ficha") Ficha ficha,
                                BindingResult result,
                                @RequestParam("programaId") Long programaId,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("Intentando guardar ficha: {}", ficha.getCodigo());
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar ficha");
            model.addAttribute("programas", programaService.getAllProgramas());
            model.addAttribute("accion", ficha.getId() == null ? "Crear" : "Editar");
            return "fichas/formulario";
        }
        
        try {
            // Asignar el programa a la ficha
            ficha.setPrograma(programaService.getProgramaById(programaId));
            
            if (ficha.getId() == null) {
                fichaService.createFicha(ficha);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Ficha creada exitosamente");
            } else {
                fichaService.updateFicha(ficha.getId(), ficha);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Ficha actualizada exitosamente");
            }
            return "redirect:/fichas";
        } catch (DuplicateResourceException e) {
            log.error("Error: Recurso duplicado", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("programas", programaService.getAllProgramas());
            model.addAttribute("accion", ficha.getId() == null ? "Crear" : "Editar");
            return "fichas/formulario";
        } catch (Exception e) {
            log.error("Error al guardar ficha", e);
            model.addAttribute("error", "Error al guardar la ficha: " + e.getMessage());
            model.addAttribute("programas", programaService.getAllProgramas());
            model.addAttribute("accion", ficha.getId() == null ? "Crear" : "Editar");
            return "fichas/formulario";
        }
    }

    /**
     * Ver detalles de una ficha
     */
    @GetMapping("/ver/{id}")
    public String verFicha(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        log.info("Mostrando detalles de la ficha ID: {}", id);

        try {
            Ficha ficha = fichaService.getFichaById(id);
            model.addAttribute("ficha", ficha);
            return "fichas/detalle";
        } catch (ResourceNotFoundException e) {
            log.error("Ficha no encontrada", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/fichas";
        }
    }

    /**
     * Formulario para editar ficha
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          RedirectAttributes redirectAttributes) {
        log.info("Mostrando formulario de edición para ficha ID: {}", id);

        try {
            Ficha ficha = fichaService.getFichaById(id);
            model.addAttribute("ficha", ficha);
            model.addAttribute("programas", programaService.getAllProgramas());
            model.addAttribute("accion", "Editar");
            return "fichas/formulario";
        } catch (ResourceNotFoundException e) {
            log.error("Ficha no encontrada", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/fichas";
        }
    }

    /**
     * Eliminar ficha
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarFicha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Intentando eliminar ficha ID: {}", id);
        
        try {
            fichaService.deleteFicha(id);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "Ficha eliminada exitosamente");
        } catch (ResourceNotFoundException e) {
            log.error("Ficha no encontrada", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error al eliminar ficha", e);
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar la ficha: " + e.getMessage());
        }
        
        return "redirect:/fichas";
    }
}
