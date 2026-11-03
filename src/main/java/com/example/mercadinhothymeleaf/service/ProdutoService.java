package com.example.mercadinhothymeleaf.service;

import com.example.mercadinhothymeleaf.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProdutoService {

    // Lista em memoria, de responsabilidade do service (thread-safe)
    private final List<Produto> produtos = new CopyOnWriteArrayList<>();

    // Sem termo, devolve tudo; com termo, filtra por nome ou categoria
    public List<Produto> listar(String busca) {
        if (busca == null || busca.isBlank()) {
            return List.copyOf(produtos);
        }
        String termo = busca.trim().toLowerCase(Locale.ROOT);
        return produtos.stream()
                .filter(p -> contem(p.getNome(), termo) || contem(p.getCategoria(), termo))
                .toList();
    }

    public void adicionar(Produto produto) {
        BigDecimal preco = produto.getPreco();
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo. O produto não foi cadastrado.");
        }
        produtos.add(produto);
    }

    public void remover(String id) {
        produtos.removeIf(p -> p.getId().equals(id));
    }

    private boolean contem(String texto, String termo) {
        return texto != null && texto.toLowerCase(Locale.ROOT).contains(termo);
    }
}
