# Mercadinho Thymeleaf

Aplicacao web de cadastro de produtos feita com **Spring Boot** e **Thymeleaf**, como exercicio da aula *Cadastro de Produtos com Thymeleaf*.

O usuario cadastra produtos num formulario, os dados sao gravados num banco **H2 embarcado** (via Spring Data JPA) e uma segunda pagina mostra os produtos numa tabela, com busca e remocao.

## Tecnologias

- Java 17
- Spring Boot 3.5.7 (Spring Web, Thymeleaf e Spring Data JPA)
- Banco H2 em memoria
- Maven
- Bootstrap 4.6.2 (via CDN)

## Estrutura

```
src/main/java/com/example/mercadinhothymeleaf/
  MercadinhoThymeleafApplication.java   ponto de entrada
  model/Produto.java                    entidade JPA (tabela produtos)
  repository/ProdutoRepository.java     acesso ao banco (Spring Data JPA)
  service/ProdutoService.java           regras de negocio
  controller/ProdutoController.java     rotas e fluxo
src/main/resources/
  templates/cadastrar.html              formulario
  templates/lista.html                  tabela, busca e remocao
  static/css/estilo.css                 estilo proprio
  application.properties                configuracao
src/test/java/...                       testes automatizados (service e controller)
postman/                                colecao do Postman
```

O projeto segue o padrao **MVC** em camadas: o Controller recebe a requisicao, o Service aplica as regras, o Repository fala com o banco e a View (Thymeleaf) monta a pagina.

## Campos do produto

| Campo | Regra |
|---|---|
| nome | obrigatorio |
| preco | obrigatorio e nao pode ser negativo (`BigDecimal`) |
| categoria | escolhida numa lista fixa: Eletronico, Vestuario, Alimento, Outros |
| estoque | numero inteiro, minimo 0 |

## Rotas

| Metodo | Rota | O que faz |
|---|---|---|
| GET | `/` | Redireciona para `/cadastrar` |
| GET | `/cadastrar` | Mostra o formulario |
| POST | `/cadastrar` | Valida e salva o produto; se o preco for negativo, volta ao formulario com erro |
| GET | `/lista` | Mostra a tabela de produtos |
| GET | `/lista?busca=termo` | Filtra por nome ou categoria, sem diferenciar maiusculas |
| POST | `/remover/{id}` | Remove o produto pelo id |

Apos um POST bem-sucedido o servidor redireciona (padrao Post/Redirect/Get), o que evita duplicar o cadastro ao recarregar a pagina.

## Como rodar

Pre-requisitos: JDK 17 ou superior e Maven 3.8+.

```bash
git clone https://github.com/DevTroli/mercadinho-thymeleaf.git
cd mercadinho-thymeleaf
mvn spring-boot:run
```

Aguarde a mensagem `Started MercadinhoThymeleafApplication` e acesse http://localhost:8080.

Se a porta 8080 estiver ocupada, adicione `server.port=8081` em `src/main/resources/application.properties`.

### Rodando no GitHub Codespaces

O Codespaces expoe a porta 8080 atras de um proxy. As duas linhas `server.forward-headers-strategy` e `server.tomcat.use-relative-redirects` do `application.properties` fazem os redirecionamentos funcionarem la.

## Roteiro de teste manual

1. Abra `/`: deve redirecionar para `/cadastrar`.
2. Cadastre `Camiseta` (Vestuario), `Notebook` (Eletronico) e `Arroz` (Alimento).
3. Em `/lista`, busque `note`: so o Notebook aparece. Busque `vest`: so a Camiseta aparece.
4. Cadastre um produto com preco `-5`: o produto nao e salvo e aparece uma mensagem de erro.
5. Remova um produto e confira que os outros continuam na lista.
6. Reinicie o servidor e abra `/lista`: aparece "Nenhum produto cadastrado ainda." (o H2 esta em memoria).

## Testes

### Testes automatizados

```bash
mvn test
```

Nao e preciso subir o servidor. Os testes sobem o contexto do Spring com o H2 em memoria e cobrem:

- **ProdutoServiceTest:** salvar, preco negativo, nulo e zero, busca por nome e por categoria, busca sem diferenciar maiusculas, busca sem resultado e remocao por id.
- **ProdutoControllerTest (MockMvc):** redirecionamento da raiz, formulario, cadastro valido, preco negativo, lista, busca, busca sem resultado e remocao.

### Colecao do Postman

O arquivo `postman/mercadinho-thymeleaf.postman_collection.json` testa o fluxo completo com a aplicacao rodando.

1. Suba a aplicacao (`mvn spring-boot:run`).
2. No Postman, clique em **Import** e escolha o arquivo da pasta `postman/`.
3. Abra a colecao, va na aba **Variables** e confira o `baseUrl`. Use `http://localhost:8080`, ou o endereco da porta no Codespaces, **sem barra no final**.
4. Clique em **Run** e depois em **Start run** (ou **Run Mercadinho Thymeleaf**).

Sao 9 passos: raiz, formulario, cadastro valido, preco negativo, listar (guarda o id do produto), buscar, busca sem resultado, remover e conferir a remocao. Os redirecionamentos ficam desativados de proposito, para o Postman conferir o status 302.

No Codespaces, a porta 8080 precisa estar com visibilidade **Public** para o Postman acessar de fora.

## Banco H2

O H2 roda dentro da aplicacao, em memoria (`jdbc:h2:mem:mercadinho`), e a tabela e criada sozinha a partir da entidade. Os dados somem quando o servidor reinicia.

Para ver os dados, abra http://localhost:8080/h2-console com:

- JDBC URL: `jdbc:h2:mem:mercadinho`
- User: `sa`
- Password: em branco

O console e so para estudo e deve ser desativado (`spring.h2.console.enabled=false`) em qualquer ambiente real.

Para guardar os dados em arquivo, troque a URL por `jdbc:h2:file:./dados/mercadinho` e o `ddl-auto` por `update`.

## Exercicios da aula concluidos

- **Nivel 1:** campo estoque, CSS proprio e total de produtos.
- **Nivel 2:** dropdown de categoria, validacao de preco negativo e botao Remover.
- **Nivel 3:** `ProdutoService`, busca por nome ou categoria e Spring Data JPA com H2.

## Decisoes

- O preco usa `BigDecimal` em vez de `double`, para evitar erros de arredondamento com dinheiro.
- A remocao usa o `id` do banco, e nao a posicao na lista, para nao apagar o produto errado quando a lista esta filtrada.
