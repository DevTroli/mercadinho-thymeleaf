package com.example.mercadinhothymeleaf.repository;

import com.example.mercadinhothymeleaf.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // O Spring Data gera a consulta a partir do nome do metodo:
    // WHERE lower(nome) LIKE %termo% OR lower(categoria) LIKE %termo%
    List<Produto> findByNomeContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String nome, String categoria);
}
