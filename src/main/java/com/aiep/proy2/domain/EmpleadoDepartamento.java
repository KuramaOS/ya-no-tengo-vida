package com.aiep.proy2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A EmpleadoDepartamento.
 */
@Entity
@Table(name = "empleado_departamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoDepartamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_empleado", nullable = false)
    private String idEmpleado;

    @NotNull
    @Column(name = "id_departamento", nullable = false)
    private String idDepartamento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmpleadoDepartamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdEmpleado() {
        return this.idEmpleado;
    }

    public EmpleadoDepartamento idEmpleado(String idEmpleado) {
        this.setIdEmpleado(idEmpleado);
        return this;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdDepartamento() {
        return this.idDepartamento;
    }

    public EmpleadoDepartamento idDepartamento(String idDepartamento) {
        this.setIdDepartamento(idDepartamento);
        return this;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public EmpleadoDepartamento empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public EmpleadoDepartamento departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoDepartamento)) {
            return false;
        }
        return getId() != null && getId().equals(((EmpleadoDepartamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDepartamento{" +
            "id=" + getId() +
            ", idEmpleado='" + getIdEmpleado() + "'" +
            ", idDepartamento='" + getIdDepartamento() + "'" +
            "}";
    }
}
