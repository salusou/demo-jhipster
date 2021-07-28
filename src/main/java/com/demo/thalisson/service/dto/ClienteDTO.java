package com.demo.thalisson.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.demo.thalisson.domain.Cliente} entity.
 */
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    private String endereco;

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
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", compra=" + getCompra() +
            "}";
    }
}
