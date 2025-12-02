package com.sena.controller;

import com.sena.exception.DuplicateResourceException;
import com.sena.exception.ResourceNotFoundException;
import com.sena.model.Aprendiz;
import com.sena.service.AprendizService;
import com.sena.service.FichaService;
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
 * Controlador web para gestión de Aprendices
 * Maneja todas las peticiones HTTP relacionadas con aprendices
 */
@Controller
@RequestMapping("/aprendices")
public class AprendizController {

    private static final Logger log = LoggerFactory.getLogger(AprendizController.class);

    private final AprendizService aprendizService;
    private final FichaService fichaService;

    public AprendizController(AprendizService aprendizService, FichaService fichaService) {
        this.aprendizService = aprendizService;
        this.fichaService = fichaService;
    }

    /**
     * Página principal - Lista de aprendices
     */
    @GetMapping({"", "/", "/lista"})
    public String listarAprendices(Model model) {
        log.info("Mostrando lista de aprendices");
        List<Aprendiz> aprendices = aprendizService.getAllAprendices();
        model.addAttribute("aprendices", aprendices);
        model.addAttribute("totalAprendices", aprendices.size());
        return "aprendices/lista";
    }

    /**
     * Formulario para crear nuevo aprendiz
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        log.info("Mostrando formulario de nuevo aprendiz");
        model.addAttribute("aprendiz", new Aprendiz());
        model.addAttribute("fichas", fichaService.getAllFichas());
        model.addAttribute("accion", "Crear");
        return "aprendices/formulario";
    }

    /**
     * Procesar creación de nuevo aprendiz
     */
    @PostMapping("/guardar")
    public String guardarAprendiz(@Valid @ModelAttribute("aprendiz") Aprendiz aprendiz,
                                   BindingResult result,
                                   @RequestParam("fichaId") Long fichaId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        log.info("Intentando guardar aprendiz: {}", aprendiz.getDocumento());
        
        if (result.hasErrors()) {
            log.warn("Errores de validación al guardar aprendiz");
            model.addAttribute("fichas", fichaService.getAllFichas());
            model.addAttribute("accion", aprendiz.getId() == null ? "Crear" : "Editar");
            return "aprendices/formulario";
        }
        
        try {
            // Asignar la ficha al aprendiz
            aprendiz.setFicha(fichaService.getFichaById(fichaId));
            
            if (aprendiz.getId() == null) {
                aprendizService.createAprendiz(aprendiz);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Aprendiz creado exitosamente");
            } else {
                aprendizService.updateAprendiz(aprendiz.getId(), aprendiz);
                redirectAttributes.addFlashAttribute("mensajeExito", 
                    "Aprendiz actualizado exitosamente");
            }
            return "redirect:/aprendices";
        } catch (DuplicateResourceException e) {
            log.error("Error: Recurso duplicado", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fichas", fichaService.getAllFichas());
            model.addAttribute("accion", aprendiz.getId() == null ? "Crear" : "Editar");
            return "aprendices/formulario";
        } catch (Exception e) {
            log.error("Error al guardar aprendiz", e);
            model.addAttribute("error", "Error al guardar el aprendiz: " + e.getMessage());
            model.addAttribute("fichas", fichaService.getAllFichas());
            model.addAttribute("accion", aprendiz.getId() == null ? "Crear" : "Editar");
            return "aprendices/formulario";
        }
    }

    /**
     * Formulario para editar aprendiz existente
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, 
                                          RedirectAttributes redirectAttributes) {
        log.info("Mostrando formulario de edición para aprendiz ID: {}", id);
        
        try {
            Aprendiz aprendiz = aprendizService.getAprendizById(id);
            model.addAttribute("aprendiz", aprendiz);
            model.addAttribute("fichas", fichaService.getAllFichas());
            model.addAttribute("accion", "Editar");
            return "aprendices/formulario";
        } catch (ResourceNotFoundException e) {
            log.error("Aprendiz no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/aprendices";
        }
    }

    /**
     * Ver detalles de un aprendiz
     */
    @GetMapping("/ver/{id}")
    public String verAprendiz(@PathVariable Long id, Model model, 
                              RedirectAttributes redirectAttributes) {
        log.info("Mostrando detalles del aprendiz ID: {}", id);
        
        try {
            Aprendiz aprendiz = aprendizService.getAprendizById(id);
            model.addAttribute("aprendiz", aprendiz);
            return "aprendices/detalle";
        } catch (ResourceNotFoundException e) {
            log.error("Aprendiz no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/aprendices";
        }
    }

    /**
     * Eliminar aprendiz
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarAprendiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Intentando eliminar aprendiz ID: {}", id);
        
        try {
            aprendizService.deleteAprendiz(id);
            redirectAttributes.addFlashAttribute("mensajeExito", 
                "Aprendiz eliminado exitosamente");
        } catch (ResourceNotFoundException e) {
            log.error("Aprendiz no encontrado", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error al eliminar aprendiz", e);
            redirectAttributes.addFlashAttribute("error", 
                "Error al eliminar el aprendiz: " + e.getMessage());
        }
        
        return "redirect:/aprendices";
    }

    /**
     * Buscar aprendices
     */
    @GetMapping("/buscar")
    public String buscarAprendices(@RequestParam(required = false) String searchTerm, Model model) {
        log.info("Buscando aprendices con término: {}", searchTerm);
        
        List<Aprendiz> aprendices;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            aprendices = aprendizService.searchAprendices(searchTerm);
            model.addAttribute("searchTerm", searchTerm);
        } else {
            aprendices = aprendizService.getAllAprendices();
        }
        
        model.addAttribute("aprendices", aprendices);
        model.addAttribute("totalAprendices", aprendices.size());
        return "aprendices/lista";
    }

    /**
     * Filtrar aprendices por ficha
     */
    @GetMapping("/ficha/{fichaId}")
    public String filtrarPorFicha(@PathVariable Long fichaId, Model model, 
                                   RedirectAttributes redirectAttributes) {
        log.info("Filtrando aprendices por ficha ID: {}", fichaId);
        
        try {
            List<Aprendiz> aprendices = aprendizService.getAprendicesByFichaId(fichaId);
            model.addAttribute("aprendices", aprendices);
            model.addAttribute("totalAprendices", aprendices.size());
            model.addAttribute("fichaFiltro", fichaService.getFichaById(fichaId).getCodigo());
            return "aprendices/lista";
        } catch (ResourceNotFoundException e) {
            log.error("Ficha no encontrada", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/aprendices";
        }
    }

    /**
     * Filtrar aprendices por programa
     */
    @GetMapping("/programa/{programaId}")
    public String filtrarPorPrograma(@PathVariable Long programaId, Model model) {
        log.info("Filtrando aprendices por programa ID: {}", programaId);
        
        List<Aprendiz> aprendices = aprendizService.getAprendicesByProgramaId(programaId);
        model.addAttribute("aprendices", aprendices);
        model.addAttribute("totalAprendices", aprendices.size());
        model.addAttribute("programaFiltro", programaId);
        return "aprendices/lista";
    }
}
