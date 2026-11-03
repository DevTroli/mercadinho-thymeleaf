package com.example.mercadinhothymeleaf.service;

import com.example.mercadinhothymeleaf.model.Produto;
import com.example.mercadinhothymeleaf.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProdutoServiceTest {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private Produto novoProduto(String nome, String preco, String categoria) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco == null ? null : new BigDecimal(preco));
        produto.setCategoria(categoria);
        produto.setEstoque(5);
        return produto;
    }

    @Test
    void adicionar_produtoValido_salvaComId() {
        Produto produto = novoProduto("Camiseta", "49.90", "Vestuário");

        service.adicionar(produto);

        assertThat(produto.getId()).isNotNull();
        assertThat(service.listar(null)).hasSize(1);
    }

    @Test
    void adicionar_precoNegativo_lancaExcecaoENaoSalva() {
        Produto produto = novoProduto("Camiseta", "-5", "Vestuário");

        assertThatThrownBy(() -> service.adicionar(produto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("negativo");
        assertThat(repository.count()).isZero();
    }

    @Test
    void adicionar_precoNulo_lancaExcecao() {
        Produto produto = novoProduto("Camiseta", null, "Vestuário");

        assertThatThrownBy(() -> service.adicionar(produto))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(repository.count()).isZero();
    }

    @Test
    void adicionar_precoZero_eAceito() {
        service.adicionar(novoProduto("Brinde", "0", "Outros"));

        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void listar_buscaPorNome_filtraResultados() {
        service.adicionar(novoProduto("Camiseta", "49.90", "Vestuário"));
        service.adicionar(novoProduto("Notebook", "3500", "Eletrônico"));

        List<Produto> resultado = service.listar("note");

        assertThat(resultado).extracting(Produto::getNome).containsExactly("Notebook");
    }

    @Test
    void listar_buscaPorCategoria_filtraResultados() {
        service.adicionar(novoProduto("Camiseta", "49.90", "Vestuário"));
        service.adicionar(novoProduto("Notebook", "3500", "Eletrônico"));

        List<Produto> resultado = service.listar("vest");

        assertThat(resultado).extracting(Produto::getNome).containsExactly("Camiseta");
    }

    @Test
    void listar_buscaIgnoraMaiusculas() {
        service.adicionar(novoProduto("Camiseta", "49.90", "Vestuário"));

        assertThat(service.listar("CAMISETA")).hasSize(1);
    }

    @Test
    void listar_buscaSemResultado_retornaListaVazia() {
        service.adicionar(novoProduto("Camiseta", "49.90", "Vestuário"));

        assertThat(service.listar("xyz")).isEmpty();
    }

    @Test
    void remover_apagaApenasOProdutoDoId() {
        Produto camiseta = novoProduto("Camiseta", "49.90", "Vestuário");
        Produto notebook = novoProduto("Notebook", "3500", "Eletrônico");
        service.adicionar(camiseta);
        service.adicionar(notebook);

        service.remover(camiseta.getId());

        assertThat(service.listar(null)).extracting(Produto::getNome).containsExactly("Notebook");
    }
}
