package com.demo.thalisson.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.demo.thalisson.domain.Loja} entity.
 */
public class LojaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String endereco;

    @NotNull
    @Size(max = 11)
    private String telefone;

    private CompraDTO compra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public CompraDTO getCompra() {
        return compra;
    }

    public void setCompra(CompraDTO compra) {
        this.compra = compra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LojaDTO)) {
            return false;
        }

        LojaDTO lojaDTO = (LojaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lojaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LojaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", compra=" + getCompra() +
            "}";
    }
}
