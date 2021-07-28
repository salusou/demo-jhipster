package com.demo.thalisson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compra.
 */
@Entity
@Table(name = "compra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nome_produto", nullable = false)
    private String nomeProduto;

    @NotNull
    @Column(name = "valor", precision = 21, scale = 2, nullable = false)
    private BigDecimal valor;

    @OneToMany(mappedBy = "compra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compra" }, allowSetters = true)
    private Set<Loja> lojas = new HashSet<>();

    @OneToMany(mappedBy = "compra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "compra" }, allowSetters = true)
    private Set<Cliente> clientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compra id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomeProduto() {
        return this.nomeProduto;
    }

    public Compra nomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
        return this;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Compra valor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Set<Loja> getLojas() {
        return this.lojas;
    }

    public Compra lojas(Set<Loja> lojas) {
        this.setLojas(lojas);
        return this;
    }

    public Compra addLoja(Loja loja) {
        this.lojas.add(loja);
        loja.setCompra(this);
        return this;
    }

    public Compra removeLoja(Loja loja) {
        this.lojas.remove(loja);
        loja.setCompra(null);
        return this;
    }

    public void setLojas(Set<Loja> lojas) {
        if (this.lojas != null) {
            this.lojas.forEach(i -> i.setCompra(null));
        }
        if (lojas != null) {
            lojas.forEach(i -> i.setCompra(this));
        }
        this.lojas = lojas;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public Compra clientes(Set<Cliente> clientes) {
        this.setClientes(clientes);
        return this;
    }

    public Compra addCliente(Cliente cliente) {
        this.clientes.add(cliente);
        cliente.setCompra(this);
        return this;
    }

    public Compra removeCliente(Cliente cliente) {
        this.clientes.remove(cliente);
        cliente.setCompra(null);
        return this;
    }

    public void setClientes(Set<Cliente> clientes) {
        if (this.clientes != null) {
            this.clientes.forEach(i -> i.setCompra(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setCompra(this));
        }
        this.clientes = clientes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compra)) {
            return false;
        }
        return id != null && id.equals(((Compra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compra{" +
            "id=" + getId() +
            ", nomeProduto='" + getNomeProduto() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
