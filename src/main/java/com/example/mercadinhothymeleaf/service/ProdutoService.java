package com.example.mercadinhothymeleaf.service;

import com.example.mercadinhothymeleaf.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProdutoService {

    // Lista em memoria, agora de responsabilidade do service (thread-safe)
    private final List<Produto> produtos = new CopyOnWriteArrayList<>();

    public List<Produto> listar() {
        return List.copyOf(produtos);
    }

    public void adicionar(Produto produto) {
        BigDecimal preco = produto.getPreco();
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo. O produto não foi cadastrado.");
        }
        produtos.add(produto);
    }

    public void remover(int indice) {
        if (indice >= 0 && indice < produtos.size()) {
            produtos.remove(indice);
        }
    }
}
