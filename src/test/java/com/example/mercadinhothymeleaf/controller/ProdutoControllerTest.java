package com.example.mercadinhothymeleaf.controller;

import com.example.mercadinhothymeleaf.model.Produto;
import com.example.mercadinhothymeleaf.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private Produto salvar(String nome, String preco, String categoria) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(new BigDecimal(preco));
        produto.setCategoria(categoria);
        produto.setEstoque(3);
        return repository.save(produto);
    }

    @Test
    void raiz_redirecionaParaCadastrar() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cadastrar"));
    }

    @Test
    void formulario_exibeProdutoVazioECategorias() throws Exception {
        mockMvc.perform(get("/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastrar"))
                .andExpect(model().attributeExists("produto", "categorias"))
                .andExpect(content().string(containsString("<select")));
    }

    @Test
    void cadastrar_dadosValidos_salvaERedirecionaParaLista() throws Exception {
        mockMvc.perform(post("/cadastrar")
                        .param("nome", "Camiseta")
                        .param("preco", "49.90")
                        .param("categoria", "Vestuário")
                        .param("estoque", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lista"));

        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void cadastrar_precoNegativo_voltaAoFormularioComErroENaoSalva() throws Exception {
        mockMvc.perform(post("/cadastrar")
                        .param("nome", "Camiseta")
                        .param("preco", "-5")
                        .param("categoria", "Vestuário")
                        .param("estoque", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastrar"))
                .andExpect(model().attributeExists("erro"));

        assertThat(repository.count()).isZero();
    }

    @Test
    void lista_mostraOsProdutosCadastrados() throws Exception {
        salvar("Notebook", "3500", "Eletrônico");

        mockMvc.perform(get("/lista"))
                .andExpect(status().isOk())
                .andExpect(view().name("lista"))
                .andExpect(content().string(containsString("Notebook")));
    }

    @Test
    void lista_comBusca_mostraSoOsProdutosEncontrados() throws Exception {
        salvar("Notebook", "3500", "Eletrônico");
        salvar("Camiseta", "49.90", "Vestuário");

        mockMvc.perform(get("/lista").param("busca", "note"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Notebook")))
                .andExpect(content().string(not(containsString("Camiseta"))));
    }

    @Test
    void lista_buscaSemResultado_mostraMensagemSemQuebrarAPagina() throws Exception {
        salvar("Notebook", "3500", "Eletrônico");

        mockMvc.perform(get("/lista").param("busca", "xyz"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Nenhum produto encontrado para: xyz")));
    }

    @Test
    void remover_apagaOProdutoERedirecionaParaLista() throws Exception {
        Produto produto = salvar("Notebook", "3500", "Eletrônico");

        mockMvc.perform(post("/remover/" + produto.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lista"));

        assertThat(repository.count()).isZero();
    }
}
