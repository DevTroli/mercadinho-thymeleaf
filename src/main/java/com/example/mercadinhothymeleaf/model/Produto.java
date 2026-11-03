package com.example.mercadinhothymeleaf.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Produto {

    // Identificador unico, gerado na criacao. Sem setter: o formulario nao consegue alterar.
    private final String id = UUID.randomUUID().toString();
    private String nome;
    private BigDecimal preco;
    private String categoria;
    private int estoque;

    // Construtor vazio: obrigatorio para o Spring preencher o objeto a partir do formulario
    public Produto() {
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}
