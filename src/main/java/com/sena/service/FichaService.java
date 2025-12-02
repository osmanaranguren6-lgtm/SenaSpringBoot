package com.sena.service;

import com.sena.exception.DuplicateResourceException;
import com.sena.exception.ResourceNotFoundException;
import com.sena.model.Ficha;
import com.sena.repository.FichaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de lógica de negocio para Ficha
 */
@Service
@Transactional
public class FichaService {

    private static final Logger log = LoggerFactory.getLogger(FichaService.class);

    private final FichaRepository fichaRepository;

    public FichaService(FichaRepository fichaRepository) {
        this.fichaRepository = fichaRepository;
    }

    /**
     * Obtener todas las fichas
     */
    @Transactional(readOnly = true)
    public List<Ficha> getAllFichas() {
        log.info("Obteniendo todas las fichas");
        return fichaRepository.findAllWithPrograma();
    }

    /**
     * Obtener ficha por ID
     */
    @Transactional(readOnly = true)
    public Ficha getFichaById(Long id) {
        log.info("Buscando ficha con ID: {}", id);
        return fichaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha no encontrada con ID: " + id));
    }

    /**
     * Buscar ficha por código
     */
    @Transactional(readOnly = true)
    public Ficha getFichaByCodigo(String codigo) {
        log.info("Buscando ficha con código: {}", codigo);
        return fichaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha no encontrada con código: " + codigo));
    }

    /**
     * Crear nueva ficha
     */
    public Ficha createFicha(Ficha ficha) {
        log.info("Creando nueva ficha: {}", ficha.getCodigo());
        
        // Validar que no exista el código
        if (fichaRepository.existsByCodigo(ficha.getCodigo())) {
            throw new DuplicateResourceException("Ya existe una ficha con el código: " + ficha.getCodigo());
        }
        
        return fichaRepository.save(ficha);
    }

    /**
     * Actualizar ficha existente
     */
    public Ficha updateFicha(Long id, Ficha fichaActualizada) {
        log.info("Actualizando ficha con ID: {}", id);
        
        Ficha fichaExistente = getFichaById(id);
        
        // Validar código duplicado (si cambió)
        if (!fichaExistente.getCodigo().equals(fichaActualizada.getCodigo()) &&
            fichaRepository.existsByCodigo(fichaActualizada.getCodigo())) {
            throw new DuplicateResourceException("Ya existe una ficha con el código: " + fichaActualizada.getCodigo());
        }
        
        fichaExistente.setCodigo(fichaActualizada.getCodigo());
        fichaExistente.setPrograma(fichaActualizada.getPrograma());
        
        return fichaRepository.save(fichaExistente);
    }

    /**
     * Eliminar ficha
     */
    public void deleteFicha(Long id) {
        log.info("Eliminando ficha con ID: {}", id);
        
        if (!fichaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ficha no encontrada con ID: " + id);
        }
        
        fichaRepository.deleteById(id);
    }

    /**
     * Buscar fichas por término de búsqueda
     */
    @Transactional(readOnly = true)
    public List<Ficha> searchFichas(String searchTerm) {
        log.info("Buscando fichas con término: {}", searchTerm);
        return fichaRepository.searchByCodigo(searchTerm);
    }

    /**
     * Obtener fichas por programa
     */
    @Transactional(readOnly = true)
    public List<Ficha> getFichasByProgramaId(Long programaId) {
        log.info("Obteniendo fichas del programa ID: {}", programaId);
        return fichaRepository.findByProgramaId(programaId);
    }

    /**
     * Obtener total de fichas
     */
    @Transactional(readOnly = true)
    public long getTotalFichas() {
        return fichaRepository.count();
    }
}
