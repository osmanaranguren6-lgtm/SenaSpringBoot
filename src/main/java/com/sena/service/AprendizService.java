package com.sena.service;

import com.sena.exception.ResourceNotFoundException;
import com.sena.exception.DuplicateResourceException;
import com.sena.model.Aprendiz;
import com.sena.repository.AprendizRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de lógica de negocio para Aprendiz
 * Maneja todas las operaciones CRUD y validaciones de negocio
 */
@Service
@Transactional
public class AprendizService {

    private static final Logger log = LoggerFactory.getLogger(AprendizService.class);

    private final AprendizRepository aprendizRepository;

    public AprendizService(AprendizRepository aprendizRepository) {
        this.aprendizRepository = aprendizRepository;
    }

    /**
     * Obtener todos los aprendices con sus relaciones
     */
    @Transactional(readOnly = true)
    public List<Aprendiz> getAllAprendices() {
        log.info("Obteniendo todos los aprendices");
        return aprendizRepository.findAllWithFichaAndPrograma();
    }

    /**
     * Obtener aprendiz por ID
     */
    @Transactional(readOnly = true)
    public Aprendiz getAprendizById(Long id) {
        log.info("Buscando aprendiz con ID: {}", id);
        return aprendizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aprendiz no encontrado con ID: " + id));
    }

    /**
     * Buscar aprendiz por documento
     */
    @Transactional(readOnly = true)
    public Aprendiz getAprendizByDocumento(String documento) {
        log.info("Buscando aprendiz con documento: {}", documento);
        return aprendizRepository.findByDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException("Aprendiz no encontrado con documento: " + documento));
    }

    /**
     * Crear nuevo aprendiz
     */
    public Aprendiz createAprendiz(Aprendiz aprendiz) {
        log.info("Creando nuevo aprendiz: {}", aprendiz.getDocumento());
        
        // Validar que no exista el documento
        if (aprendizRepository.existsByDocumento(aprendiz.getDocumento())) {
            throw new DuplicateResourceException("Ya existe un aprendiz con el documento: " + aprendiz.getDocumento());
        }
        
        // Validar que no exista el correo
        if (aprendizRepository.existsByCorreo(aprendiz.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un aprendiz con el correo: " + aprendiz.getCorreo());
        }
        
        return aprendizRepository.save(aprendiz);
    }

    /**
     * Actualizar aprendiz existente
     */
    public Aprendiz updateAprendiz(Long id, Aprendiz aprendizActualizado) {
        log.info("Actualizando aprendiz con ID: {}", id);
        
        Aprendiz aprendizExistente = getAprendizById(id);
        
        // Validar documento duplicado (si cambió)
        if (!aprendizExistente.getDocumento().equals(aprendizActualizado.getDocumento()) &&
            aprendizRepository.existsByDocumento(aprendizActualizado.getDocumento())) {
            throw new DuplicateResourceException("Ya existe un aprendiz con el documento: " + aprendizActualizado.getDocumento());
        }
        
        // Validar correo duplicado (si cambió)
        if (!aprendizExistente.getCorreo().equals(aprendizActualizado.getCorreo()) &&
            aprendizRepository.existsByCorreo(aprendizActualizado.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un aprendiz con el correo: " + aprendizActualizado.getCorreo());
        }
        
        // Actualizar campos
        aprendizExistente.setDocumento(aprendizActualizado.getDocumento());
        aprendizExistente.setNombres(aprendizActualizado.getNombres());
        aprendizExistente.setApellidos(aprendizActualizado.getApellidos());
        aprendizExistente.setCorreo(aprendizActualizado.getCorreo());
        aprendizExistente.setTelefono(aprendizActualizado.getTelefono());
        aprendizExistente.setFicha(aprendizActualizado.getFicha());
        
        return aprendizRepository.save(aprendizExistente);
    }

    /**
     * Eliminar aprendiz
     */
    public void deleteAprendiz(Long id) {
        log.info("Eliminando aprendiz con ID: {}", id);
        
        if (!aprendizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aprendiz no encontrado con ID: " + id);
        }
        
        aprendizRepository.deleteById(id);
    }

    /**
     * Buscar aprendices por término de búsqueda
     */
    @Transactional(readOnly = true)
    public List<Aprendiz> searchAprendices(String searchTerm) {
        log.info("Buscando aprendices con término: {}", searchTerm);
        return aprendizRepository.searchByNombresOrApellidosOrDocumento(searchTerm);
    }

    /**
     * Obtener aprendices por ficha
     */
    @Transactional(readOnly = true)
    public List<Aprendiz> getAprendicesByFichaId(Long fichaId) {
        log.info("Obteniendo aprendices de la ficha ID: {}", fichaId);
        return aprendizRepository.findByFichaId(fichaId);
    }

    /**
     * Obtener aprendices por programa
     */
    @Transactional(readOnly = true)
    public List<Aprendiz> getAprendicesByProgramaId(Long programaId) {
        log.info("Obteniendo aprendices del programa ID: {}", programaId);
        return aprendizRepository.findByProgramaId(programaId);
    }

    /**
     * Obtener total de aprendices
     */
    @Transactional(readOnly = true)
    public long getTotalAprendices() {
        return aprendizRepository.count();
    }
}
