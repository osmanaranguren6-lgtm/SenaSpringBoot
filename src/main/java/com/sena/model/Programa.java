package com.sena.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programas")
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @NotBlank
    @Size(min = 3, max = 200)
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    // Relación One-to-Many con Ficha
    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ficha> fichas = new ArrayList<>();

    @NotBlank
    @Size(max = 50)
    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel;

    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado = "ACTIVO";

    @Column(name = "fecha_creacion", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion;

    public Programa() {}

    public Programa(String nombre) { this.nombre = nombre; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Ficha> getFichas() { return fichas; }
    public void setFichas(List<Ficha> fichas) { this.fichas = fichas; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public void addFicha(Ficha ficha) {
        fichas.add(ficha);
        ficha.setPrograma(this);
    }

    public void removeFicha(Ficha ficha) {
        fichas.remove(ficha);
        ficha.setPrograma(null);
    }
}
