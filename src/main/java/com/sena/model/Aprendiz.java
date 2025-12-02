package com.sena.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "aprendices")
public class Aprendiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @NotBlank
    @Size(min = 5, max = 20)
    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String documento;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String correo;

    @Size(min = 7, max = 20)
    @Column(length = 20)
    private String telefono;

    @Size(max = 200)
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado = "ACTIVO";

    // Relación Many-to-One con Ficha
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_id", nullable = false)
    private Ficha ficha;

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime fechaRegistro;

    // Método auxiliar para obtener el nombre completo
    @Transient
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    // Método auxiliar para obtener el código de ficha
    @Transient
    public String getCodigoFicha() {
        return ficha != null ? ficha.getCodigo() : "";
    }

    // Método auxiliar para obtener el nombre del programa
    @Transient
    public String getNombrePrograma() {
        return ficha != null && ficha.getPrograma() != null ? 
               ficha.getPrograma().getNombre() : "";
    }

    public Aprendiz() {}

    public Aprendiz(String documento, String nombres, String apellidos,
                    String correo, String telefono, Ficha ficha) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.ficha = ficha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Ficha getFicha() { return ficha; }
    public void setFicha(Ficha ficha) { this.ficha = ficha; }
    public java.time.LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.time.LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
