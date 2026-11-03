package com.example.mercadinhothymeleaf.controller;

import com.example.mercadinhothymeleaf.model.Produto;
import com.example.mercadinhothymeleaf.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProdutoController {

    private static final List<String> CATEGORIAS =
            List.of("Eletrônico", "Vestuário", "Alimento", "Outros");

    private final ProdutoService produtoService;

    // Injecao por construtor: o Spring entrega o ProdutoService pronto
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

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

    // POST /cadastrar -> delega ao service e redireciona (padrao PRG)
    @PostMapping("/cadastrar")
    public String cadastrarProduto(@ModelAttribute Produto produto, Model model) {
        try {
            produtoService.adicionar(produto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastrar";
        }
        return "redirect:/lista";
    }

    // GET /lista -> exibe todos os produtos cadastrados
    @GetMapping("/lista")
    public String exibirLista(Model model) {
        model.addAttribute("produtos", produtoService.listar());
        return "lista";
    }

    // POST /remover/{indice} -> remove o produto da posicao indicada
    @PostMapping("/remover/{indice}")
    public String removerProduto(@PathVariable int indice) {
        produtoService.remover(indice);
        return "redirect:/lista";
    }
}
