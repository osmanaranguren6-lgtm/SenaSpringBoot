package com.sena.service;

import com.sena.exception.DuplicateResourceException;
import com.sena.exception.ResourceNotFoundException;
import com.sena.model.Programa;
import com.sena.repository.ProgramaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de lógica de negocio para Programa
 */
@Service
@Transactional
public class ProgramaService {

    private static final Logger log = LoggerFactory.getLogger(ProgramaService.class);

    private final ProgramaRepository programaRepository;

    public ProgramaService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    /**
     * Obtener todos los programas
     */
    @Transactional(readOnly = true)
    public List<Programa> getAllProgramas() {
        log.info("Obteniendo todos los programas");
        return programaRepository.findAllByOrderByNombreAsc();
    }

    /**
     * Obtener programa por ID
     */
    @Transactional(readOnly = true)
    public Programa getProgramaById(Long id) {
        log.info("Buscando programa con ID: {}", id);
        return programaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado con ID: " + id));
    }

    /**
     * Buscar programa por nombre
     */
    @Transactional(readOnly = true)
    public Programa getProgramaByNombre(String nombre) {
        log.info("Buscando programa con nombre: {}", nombre);
        return programaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado con nombre: " + nombre));
    }

    /**
     * Crear nuevo programa
     */
    public Programa createPrograma(Programa programa) {
        log.info("Creando nuevo programa: {}", programa.getNombre());
        
        // Validar que no exista el nombre
        if (programaRepository.existsByNombre(programa.getNombre())) {
            throw new DuplicateResourceException("Ya existe un programa con el nombre: " + programa.getNombre());
        }
        
        return programaRepository.save(programa);
    }

    /**
     * Actualizar programa existente
     */
    public Programa updatePrograma(Long id, Programa programaActualizado) {
        log.info("Actualizando programa con ID: {}", id);
        
        Programa programaExistente = getProgramaById(id);
        
        // Validar nombre duplicado (si cambió)
        if (!programaExistente.getNombre().equals(programaActualizado.getNombre()) &&
            programaRepository.existsByNombre(programaActualizado.getNombre())) {
            throw new DuplicateResourceException("Ya existe un programa con el nombre: " + programaActualizado.getNombre());
        }
        
        programaExistente.setNombre(programaActualizado.getNombre());
        
        return programaRepository.save(programaExistente);
    }

    /**
     * Eliminar programa
     */
    public void deletePrograma(Long id) {
        log.info("Eliminando programa con ID: {}", id);
        
        if (!programaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programa no encontrado con ID: " + id);
        }
        
        programaRepository.deleteById(id);
    }

    /**
     * Buscar programas por nombre
     */
    @Transactional(readOnly = true)
    public List<Programa> searchProgramas(String searchTerm) {
        log.info("Buscando programas con término: {}", searchTerm);
        return programaRepository.searchByNombre(searchTerm);
    }

    /**
     * Obtener total de programas
     */
    @Transactional(readOnly = true)
    public long getTotalProgramas() {
        return programaRepository.count();
    }
}
