# Controle Pedagógico

Projeto de sistema para gestão pedagógica, com suporte a CRUD de alunos, professores, aulas, livros e turmas.

## Detalhes

O projeto busca gerir as informações pedagógicas de alunos seguindo o padrão da Wizard, visto que atuo como professor nessa empresa. A aplicação segue o padrão REST com autenticação via token JWT, usando Java e Spring Boot como plataforma e MySQL como database, além de usar H2 como banco de dados para testes, executados via JUnit. A arquitetura da aplicação é dividida em: controller - controladores e comunicação HTTP, domain - regras de negócio e abstrações, e infra - configurações e segurança. 

## Enums usados

Faixa etária:

- TODDLER
- LITTLE_KIDS
- KIDS
- TEENS
- ADULTS

Idioma:

- INGLES
- PORTUGUES
- ESPANHOL
- ALEMAO
- ITALIANO
- FRANCES
- CHINES
- JAPONES

Nível:

- BASICO
- INTERMEDIARIO
- AVANCADO
- FLUENTE

Cargo de professor:

- INSTRUTOR_AUXILIAR_I
- INSTRUTOR_AUXILIAR_II
- INSTRUTOR_AUXILIAR_III
- INSTRUTOR_I
- INSTRUTOR_II
- INSTRUTOR_II

Status da aula:

- DADA
- CANCELADA
- FALTA

Conceitos:

- REGULAR
- BOM
- MUITO_BOM
- OTIMO

Status do aluno:

- ATIVO
- CURSO_TRANCADO
- CURSO_CANCELADO
- CURSO_CONCLUIDO

## Endpoints

### GET /livros

Listagem paginada que retorna o nome e o nível dos livros. O idioma pode ser passado como query para refinar a busca. 

> Exemplo: /livros?idioma=ESPANHOL

```yaml
{
  "content": [
    {
      "nome": "E2",
      "nivel": "BASICO"
    },
    {
      "nome": "E4",
      "nivel": "INTERMEDIARIO"
    },
    {
      "nome": "E6",
      "nivel": "AVANCADO"
    }
  ],
  "pageable": {
    ...
  }
}
``` 

### POST /livros

Recebe o nome, idioma, faixa etária e nível de um livro para assim cadastrar um novo registro no banco.

> Exemplo de corpo da requisição:

```yaml
{
	"nome": "E6",
	"nivel": "AVANCADO",
	"faixaEtaria": "ADULTS",
	"idioma": "ESPANHOL"
}
``` 

### GET /livros/{nome}

Dado o nome de um livro, retorna todas as suas informações.

> Exemplo: /livros/W2

```yaml
{
	"nome": "W2",
	"idioma": "INGLES",
	"nivel": "BASICO",
	"faixaEtaria": "ADULTS"
}
```

### DELETE /livros/{nome}

Deleta o livro pelo nome.

### GET /professores

Retorna uma lista paginada com os professores, ordenados pelo nome em ordem alfabética. O idioma pode ser fornecido como query. 

> Exemplo: /professores?idioma=INGLES

```yaml
{
	"content": [
          {
            "id": 1,
            "nome": "Breno Morim",
            "telefone": "11 91234-5678",
            "idioma": "INGLES"
          },
          {
            "id": 2,
            "nome": "Joãozinho",
            "telefone": "+55 11 91234-5679",
            "idioma": "ESPANHOL"
          }
	],
	"pageable": {
      ...
    }
}
``` 

### GET /professores/busca

Busca avançada que retorna os dados detalhados de um professor, recebe telefone, email ou nome como parâmetros da URL, no mínimo um desses três atributos devem ser fornecidos.

> Exemplo: /professores/busca?nome=breno morim

```yaml
{
	"id": 1,
	"nome": "Breno Morim",
	"cpf": "12345678910",
	"email": "brenomorim@email.com",
	"telefone": "11 91234-5678",
	"dataAdmissao": "2023-07-05",
	"idioma": "INGLES",
	"cargo": "INSTRUTOR_AUXILIAR_II"
}
``` 

### GET /professores/{id}

Retorna os dados detalhados de um professor a partir de seu id. O retorno dos dados é igual ao endpoint anterior.

### POST /professores

Cadastra um novo professor no banco de dados, devem ser fornecidos os dados: nome, email, telefone, cpf, idioma e cargo. A data de admissão é opcional, o valor padrão é a data atual.

> Exemplo de corpo da requisição: 

```yaml
{
	"nome": "Breno Morim",
	"email": "breno@email.com",
	"telefone": "11 91234-5678",
	"cpf": "12345678911",
	"idioma": "INGLES",
	"cargo": "INSTRUTOR_AUXILIAR_II"
}
```

### PUT /professores/{id}

Atualiza o cadastro do professor, podem ser fornecidos os campos: email, telefone, cargo e idioma.

> Exemplo de corpo de requisição:

```yaml
{
	"email": "brenomorim@email.com",
	"telefone": "+55 11 91234-5678",
	"idioma": "ESPANHOL",
	"cargo": "INSTRUTOR_I"
}
```

### DELETE /professores/{id}

Deleta o registro do professor pelo id. Retorna 204 em caso de sucesso.

---
