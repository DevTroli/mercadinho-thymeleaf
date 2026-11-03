package com.example.mercadinhothymeleaf.model;

import java.math.BigDecimal;

public class Produto {

    private String nome;
    private BigDecimal preco;
    private String categoria;

    // Construtor vazio: obrigatorio para o Spring preencher o objeto a partir do formulario
    public Produto() {
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
}
