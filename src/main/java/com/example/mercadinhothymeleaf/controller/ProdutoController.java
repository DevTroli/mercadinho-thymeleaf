package com.example.mercadinhothymeleaf.controller;

import com.example.mercadinhothymeleaf.model.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProdutoController {

    // Lista estatica: existe enquanto a aplicacao estiver rodando
    private static final List<Produto> produtos = new ArrayList<>();

    private static final List<String> CATEGORIAS =
            List.of("Eletrônico", "Vestuário", "Alimento", "Outros");

    // Disponibiliza as categorias para todas as views deste controller
    @ModelAttribute("categorias")
    public List<String> categorias() {
        return CATEGORIAS;
    }

    // GET / -> redireciona para /cadastrar
    @GetMapping("/")
    public String index() {
        return "redirect:/cadastrar";
    }

    // GET /cadastrar -> exibe o formulario vazio
    @GetMapping("/cadastrar")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "cadastrar";
    }

    // POST /cadastrar -> valida, salva e redireciona (padrao PRG)
    @PostMapping("/cadastrar")
    public String cadastrarProduto(@ModelAttribute Produto produto, Model model) {
        BigDecimal preco = produto.getPreco();
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("erro", "O preço não pode ser negativo. O produto não foi cadastrado.");
            return "cadastrar";
        }
        produtos.add(produto);
        return "redirect:/lista";
    }

    // GET /lista -> exibe todos os produtos cadastrados
    @GetMapping("/lista")
    public String exibirLista(Model model) {
        model.addAttribute("produtos", produtos);
        return "lista";
    }
}
