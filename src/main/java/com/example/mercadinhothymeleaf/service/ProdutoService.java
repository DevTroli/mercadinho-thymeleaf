package com.example.mercadinhothymeleaf.service;

import com.example.mercadinhothymeleaf.model.Produto;
import com.example.mercadinhothymeleaf.repository.ProdutoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    // Sem termo, devolve tudo; com termo, filtra por nome ou categoria
    public List<Produto> listar(String busca) {
        if (busca == null || busca.isBlank()) {
            return repository.findAll(Sort.by("id"));
        }
        String termo = busca.trim();
        return repository.findByNomeContainingIgnoreCaseOrCategoriaContainingIgnoreCase(termo, termo);
    }

    public void adicionar(Produto produto) {
        BigDecimal preco = produto.getPreco();
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo. O produto não foi cadastrado.");
        }
        repository.save(produto);
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }
}
