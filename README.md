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

## Roteiro de teste

1. Abra `/`: deve redirecionar para `/cadastrar`.
2. Cadastre `Camiseta` (Vestuario), `Notebook` (Eletronico) e `Arroz` (Alimento).
3. Em `/lista`, busque `note`: so o Notebook aparece. Busque `vest`: so a Camiseta aparece.
4. Cadastre um produto com preco `-5`: o produto nao e salvo e aparece uma mensagem de erro.
5. Remova um produto e confira que os outros continuam na lista.
6. Reinicie o servidor e abra `/lista`: aparece "Nenhum produto cadastrado ainda." (o H2 esta em memoria).

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
