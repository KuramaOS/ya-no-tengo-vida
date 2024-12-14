package com.aiep.proy2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DepartamentoJefe.
 */
@Entity
@Table(name = "departamento_jefe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartamentoJefe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_departamento", nullable = false)
    private String idDepartamento;

    @NotNull
    @Column(name = "id_jefe", nullable = false)
    private String idJefe;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    private Jefe jefe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepartamentoJefe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdDepartamento() {
        return this.idDepartamento;
    }

    public DepartamentoJefe idDepartamento(String idDepartamento) {
        this.setIdDepartamento(idDepartamento);
        return this;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getIdJefe() {
        return this.idJefe;
    }

    public DepartamentoJefe idJefe(String idJefe) {
        this.setIdJefe(idJefe);
        return this;
    }

    public void setIdJefe(String idJefe) {
        this.idJefe = idJefe;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public DepartamentoJefe departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public Jefe getJefe() {
        return this.jefe;
    }

    public void setJefe(Jefe jefe) {
        this.jefe = jefe;
    }

    public DepartamentoJefe jefe(Jefe jefe) {
        this.setJefe(jefe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartamentoJefe)) {
            return false;
        }
        return getId() != null && getId().equals(((DepartamentoJefe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartamentoJefe{" +
            "id=" + getId() +
            ", idDepartamento='" + getIdDepartamento() + "'" +
            ", idJefe='" + getIdJefe() + "'" +
            "}";
    }
}
