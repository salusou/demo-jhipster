package com.demo.thalisson.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.demo.thalisson.domain.Compra} entity.
 */
public class CompraDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomeProduto;

    @NotNull
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompraDTO)) {
            return false;
        }

        CompraDTO compraDTO = (CompraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompraDTO{" +
            "id=" + getId() +
            ", nomeProduto='" + getNomeProduto() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
