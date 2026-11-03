package com.example.mercadinhothymeleaf.controller;

import com.example.mercadinhothymeleaf.model.Produto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProdutoController {

    // Lista estatica: existe enquanto a aplicacao estiver rodando
    private static final List<Produto> produtos = new ArrayList<>();

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

    // POST /cadastrar -> recebe os dados, salva e redireciona (padrao PRG)
    @PostMapping("/cadastrar")
    public String cadastrarProduto(@ModelAttribute Produto produto) {
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
