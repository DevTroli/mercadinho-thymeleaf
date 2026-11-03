# Mercadinho Thymeleaf

Aplicacao web de cadastro de produtos feita com **Spring Boot** e **Thymeleaf**, como exercicio da aula *Cadastro de Produtos com Thymeleaf*.

O usuario preenche um formulario (nome, preco e categoria), o servidor guarda o produto numa lista em memoria e uma segunda pagina mostra todos os produtos em uma tabela.

## Tecnologias

- Java 17
- Spring Boot 3.5.7 (Spring Web e Thymeleaf)
- Maven
- Bootstrap 4.6.2 (via CDN)

Nao ha banco de dados: os produtos ficam numa lista estatica e somem quando o servidor reinicia.

## Estrutura

```
src/main/java/com/example/mercadinhothymeleaf/
  MercadinhoThymeleafApplication.java   ponto de entrada
  model/Produto.java                    dados do produto
  controller/ProdutoController.java     rotas e fluxo
src/main/resources/templates/
  cadastrar.html                        formulario
  lista.html                            tabela de produtos
```

O projeto segue o padrao **MVC**: o Model guarda os dados, a View (Thymeleaf) mostra a pagina e o Controller decide o fluxo.

## Rotas

| Metodo | Rota | O que faz |
|---|---|---|
| GET | `/` | Redireciona para `/cadastrar` |
| GET | `/cadastrar` | Mostra o formulario |
| POST | `/cadastrar` | Salva o produto e redireciona para `/lista` |
| GET | `/lista` | Mostra a tabela de produtos |

Apos o POST, o servidor redireciona (padrao Post/Redirect/Get), o que evita duplicar o cadastro ao recarregar a pagina.

## Como rodar

Pre-requisitos: JDK 17 ou superior e Maven 3.8+.

```bash
git clone https://github.com/DevTroli/mercadinho-thymeleaf.git
cd mercadinho-thymeleaf
mvn spring-boot:run
```

Aguarde a mensagem `Started MercadinhoThymeleafApplication` e acesse http://localhost:8080.

Se a porta 8080 estiver ocupada, adicione `server.port=8081` em `src/main/resources/application.properties`.

## Roteiro de teste

1. Abra `http://localhost:8080`: deve redirecionar para `/cadastrar`.
2. Cadastre Nome `Camiseta`, Preco `49.90`, Categoria `Vestuario`.
3. Voce cai em `/lista` com o produto na tabela e a contagem de produtos.
4. Cadastre outro produto e confira os dois na tabela.
5. Reinicie o servidor e abra `/lista`: aparece "Nenhum produto cadastrado ainda."

## Diretivas Thymeleaf usadas

`th:action`, `th:object`, `th:field`, `th:text`, `th:each`, `th:if`, `th:unless` e `@{/rota}`.

## Decisoes

- O preco usa `BigDecimal` em vez de `double`, para evitar erros de arredondamento com dinheiro.
- A lista em memoria e uma simplificacao didatica; os exercicios avancados da aula a substituem por um `@Service` e depois por um banco H2.
