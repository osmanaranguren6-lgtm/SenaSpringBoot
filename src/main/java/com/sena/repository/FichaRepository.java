package com.sena.repository;

import com.sena.model.Ficha;
import com.sena.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Ficha
 */
@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    /**
     * Buscar ficha por código
     */
    Optional<Ficha> findByCodigo(String codigo);

    /**
     * Buscar fichas por programa
     */
    List<Ficha> findByPrograma(Programa programa);

    /**
     * Buscar fichas por ID de programa
     */
    List<Ficha> findByProgramaId(Long programaId);

    /**
     * Verificar si existe una ficha con un código específico
     */
    boolean existsByCodigo(String codigo);

    /**
     * Obtener todas las fichas ordenadas por código
     */
    List<Ficha> findAllByOrderByCodigoAsc();

    /**
     * Buscar fichas por código (búsqueda parcial)
     */
    @Query("SELECT f FROM Ficha f WHERE LOWER(f.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))")
    List<Ficha> searchByCodigo(String codigo);

    /**
     * Contar fichas por programa
     */
    Long countByPrograma(Programa programa);

    /**
     * Obtener fichas con sus programas (join fetch para evitar N+1)
     */
    @Query("SELECT f FROM Ficha f JOIN FETCH f.programa ORDER BY f.codigo")
    List<Ficha> findAllWithPrograma();
}
