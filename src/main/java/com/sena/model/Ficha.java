package com.sena.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fichas")
public class Ficha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "numero_ficha", nullable = false, unique = true, length = 20)
    private String codigo;

    // Relación Many-to-One con Programa
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    // Relación One-to-Many con Aprendiz
    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aprendiz> aprendices = new ArrayList<>();

    @Column(name = "fecha_inicio", nullable = false)
    private java.time.LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private java.time.LocalDate fechaFin;

    @Size(max = 20)
    @Column(name = "jornada", nullable = false, length = 20)
    private String jornada;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado = "ACTIVA";

    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    public Ficha() {}

    public Ficha(String codigo, Programa programa) {
        this.codigo = codigo;
        this.programa = programa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Programa getPrograma() { return programa; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public List<Aprendiz> getAprendices() { return aprendices; }
    public void setAprendices(List<Aprendiz> aprendices) { this.aprendices = aprendices; }
    public java.time.LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(java.time.LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public java.time.LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(java.time.LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getJornada() { return jornada; }
    public void setJornada(String jornada) { this.jornada = jornada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public void addAprendiz(Aprendiz aprendiz) {
        aprendices.add(aprendiz);
        aprendiz.setFicha(this);
    }

    public void removeAprendiz(Aprendiz aprendiz) {
        aprendices.remove(aprendiz);
        aprendiz.setFicha(null);
    }

    @Transient
    public String getNombrePrograma() { return programa != null ? programa.getNombre() : ""; }
}
