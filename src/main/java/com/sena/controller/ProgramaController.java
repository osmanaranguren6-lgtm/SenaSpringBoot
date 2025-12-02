package com.sena.controller;

import com.sena.exception.DuplicateResourceException;
import com.sena.exception.ResourceNotFoundException;
import com.sena.model.Programa;
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
 * Controlador web para gestión de Programas
 */
@Controller
@RequestMapping("/programas")
public class ProgramaController {

    private static final Logger log = LoggerFactory.getLogger(ProgramaController.class);

    private final ProgramaService programaService;

    public ProgramaController(ProgramaService programaService) {
        this.programaService = programaService;
    }

    /**
     * Lista de programas
     */
    @GetMapping({"", "/", "/lista"})
    public String listarProgramas(Model model) {
        log.info("Mostrando lista de programas");
        List<Programa> programas = programaService.getAllProgramas();
        model.addAttribute("programas", programas);
        model.addAttribute("totalProgramas", programas.size());
        return "programas/lista";
    }

    /**
     * Formulario para crear nuevo programa
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo programa");
        model.addAttribute("programa", new Programa());
        model.addAttribute("accion", "Crear");
        return "programas/formulario";
    }

    /**
     * Procesar creación/actualización de programa
     */
    @PostMapping("/guardar")
    public String guardarPrograma(@Valid @ModelAttribute("programa") Programa programa,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        log.info("Intentando guardar programa: {}", programa.getNombre());
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar programa");
            model.addAttribute("accion", programa.getId() == null ? "Crear" : "Editar");
            return "programas/formulario";
        }
        
        try {
            if (programa.getId() == null) {
                programaService.createPrograma(programa);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Programa creado exitosamente");
            } else {
                programaService.updatePrograma(programa.getId(), programa);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Programa actualizado exitosamente");
            }
            return "redirect:/programas";
        } catch (DuplicateResourceException e) {
            log.error("Error: Recurso duplicado", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accion", programa.getId() == null ? "Crear" : "Editar");
            return "programas/formulario";
        } catch (Exception e) {
            log.error("Error al guardar programa", e);
            model.addAttribute("error", "Error al guardar el programa: " + e.getMessage());
            model.addAttribute("accion", programa.getId() == null ? "Crear" : "Editar");
            return "programas/formulario";
        }
    }

    /**
     * Ver detalles de un programa
     */
    @GetMapping("/ver/{id}")
    public String verPrograma(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes) {
        log.info("Mostrando detalles del programa ID: {}", id);

        try {
            Programa programa = programaService.getProgramaById(id);
            model.addAttribute("programa", programa);
            return "programas/detalle";
        } catch (ResourceNotFoundException e) {
            log.error("Programa no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/programas";
        }
    }

    /**
     * Formulario para editar programa
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model,
                                          RedirectAttributes redirectAttributes) {
        log.info("Mostrando formulario de edición para programa ID: {}", id);

        try {
            Programa programa = programaService.getProgramaById(id);
            model.addAttribute("programa", programa);
            model.addAttribute("accion", "Editar");
            return "programas/formulario";
        } catch (ResourceNotFoundException e) {
            log.error("Programa no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/programas";
        }
    }

    /**
     * Eliminar programa
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarPrograma(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Intentando eliminar programa ID: {}", id);
        
        try {
            programaService.deletePrograma(id);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "Programa eliminado exitosamente");
        } catch (ResourceNotFoundException e) {
            log.error("Programa no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error al eliminar programa", e);
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar el programa: " + e.getMessage());
        }
        
        return "redirect:/programas";
    }
}
