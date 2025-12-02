package com.sena.repository;

import com.sena.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Programa
 */
@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    /**
     * Buscar programa por nombre
     */
    Optional<Programa> findByNombre(String nombre);

    /**
     * Buscar programas por nombre (búsqueda parcial)
     */
    @Query("SELECT p FROM Programa p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Programa> searchByNombre(String nombre);

    /**
     * Verificar si existe un programa con un nombre específico
     */
    boolean existsByNombre(String nombre);

    /**
     * Obtener todos los programas ordenados por nombre
     */
    List<Programa> findAllByOrderByNombreAsc();
}
