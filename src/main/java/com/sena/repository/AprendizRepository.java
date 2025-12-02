package com.sena.repository;

import com.sena.model.Aprendiz;
import com.sena.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Aprendiz
 * Extiende JpaRepository para operaciones CRUD automáticas
 */
@Repository
public interface AprendizRepository extends JpaRepository<Aprendiz, Long> {

    /**
     * Buscar aprendiz por documento
     */
    Optional<Aprendiz> findByDocumento(String documento);

    /**
     * Buscar aprendiz por correo
     */
    Optional<Aprendiz> findByCorreo(String correo);

    /**
     * Buscar aprendices por ficha
     */
    List<Aprendiz> findByFicha(Ficha ficha);

    /**
     * Buscar aprendices por ID de ficha
     */
    List<Aprendiz> findByFichaId(Long fichaId);

    /**
     * Buscar aprendices por código de ficha
     */
    @Query("SELECT a FROM Aprendiz a WHERE a.ficha.codigo = :codigoFicha")
    List<Aprendiz> findByCodigoFicha(@Param("codigoFicha") String codigoFicha);

    /**
     * Buscar aprendices por programa
     */
    @Query("SELECT a FROM Aprendiz a WHERE a.ficha.programa.id = :programaId")
    List<Aprendiz> findByProgramaId(@Param("programaId") Long programaId);

    /**
     * Buscar aprendices por nombres, apellidos o documento (búsqueda flexible)
     */
    @Query("SELECT a FROM Aprendiz a WHERE " +
           "LOWER(a.nombres) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.apellidos) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.documento) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Aprendiz> searchByNombresOrApellidosOrDocumento(@Param("searchTerm") String searchTerm);

    /**
     * Verificar si existe un aprendiz con un documento específico
     */
    boolean existsByDocumento(String documento);

    /**
     * Verificar si existe un aprendiz con un correo específico
     */
    boolean existsByCorreo(String correo);

    /**
     * Contar aprendices por ficha
     */
    Long countByFicha(Ficha ficha);

    /**
     * Obtener todos los aprendices con sus fichas y programas (join fetch para evitar N+1)
     */
    @Query("SELECT a FROM Aprendiz a " +
           "JOIN FETCH a.ficha f " +
           "JOIN FETCH f.programa " +
           "ORDER BY a.apellidos, a.nombres")
    List<Aprendiz> findAllWithFichaAndPrograma();

    /**
     * Buscar aprendices por nombre de programa
     */
    @Query("SELECT a FROM Aprendiz a WHERE LOWER(a.ficha.programa.nombre) LIKE LOWER(CONCAT('%', :programaNombre, '%'))")
    List<Aprendiz> searchByProgramaNombre(@Param("programaNombre") String programaNombre);
}
