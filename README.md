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

Deleta o livro pelo nome. Retorna 204 em caso de sucesso.

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
  "cargo": "INSTRUTOR_AUXILIAR_II",
  "dataAdmissao": "2023-07-05"
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

### GET /alunos

Retorna lista paginada de alunos, os seguintes parâmetros podem ser passados na URL: nome, status, faixa etária e nível.

> Exemplo: /alunos?status=ATIVO&nivel=AVANCADO

```yaml
{
  "content": [
    {
      "id": 2,
      "nome": "Larissa",
      "telefone": "11 91234-5678",
      "status": "ATIVO"
    },
    {
      "id": 3,
      "nome": "Breno",
      "telefone": "11 91234-5678",
      "status": "ATIVO"
    }
  ],
  "pageable": {
    ...
  }
}
```

### GET /alunos/{id}

Retorna os detalhes de um aluno específico, o retorno dos dados segue o seguinte formato:

> Exemplo /alunos/3
 
```yaml
{
  "nome": "Breno",
  "telefone": "11 91234-5678",
  "email": "breno@email.com",
  "cpf": "12345678910",
  "dataNascimento": "2004-02-13",
  "observacoes": null,
  "statusAluno": "ATIVO",
  "faixaEtaria": "ADULTS",
  "nivel": "AVANCADO"
}
```

### POST /alunos

Cadastra um novo aluno, precisa dos seguintes dados: nome, email, telefone, cpf, data de nascimento e faixa etária. As informações de observações e nível são opcionais, o nível padrão é básico e o status do aluno começa como ativo.

> Exemplo de corpo da requisição:
 
```yaml
{
  "nome": "Maria",
  "telefone": "11 91234-5678",
  "email": "maria@email.com",
  "cpf": "12345678910",
  "dataNascimento": "1997-03-14",
  "faixaEtaria": "ADULTS",
  "nivel": "INTERMEDIARIO"
}
```

### PUT /alunos/{id}

Altera algumas informações do aluno, os dados que podem ser alterados são: email, telefone, faixa etária, nível, status e observações.

```yaml
{
  "statusAluno": "CURSO_TRANCADO",
  "observacoes": "Trancou o curso por falta de interesse"
}
```

### DELETE /alunos/{id}

Deleta aluno do banco de dados pelo id.

### GET /aulas

Busca paginada pelas aulas, aceita filtros como parâmetros de URL, podem ser passados: nome do livro, status, id do aluno e id do professor.

> Exemplo: /aulas?status=DADA&aluno_id=1&livro=W2&professor_id=2

```yaml
{
  "content": [
    {
      "id": 3,
      "aluno": "Felipe",
      "professor": "Larissa",
      "livro": "W2",
      "licao": "13",
      "data": "2023-07-02T10:00:00",
      "statusAula": "DADA",
      "fala": "OTIMO",
      "audicao": "MUITO_BOM",
      "leitura": "OTIMO",
      "escrita": "OTIMO"
    },
    {
      "id": 2,
      "aluno": "Felipe",
      "professor": "Larissa",
      "livro": "W2",
      "licao": "12",
      "data": "2023-06-30T10:00:00",
      "statusAula": "DADA",
      "fala": "OTIMO",
      "audicao": "MUITO_BOM",
      "leitura": "OTIMO",
      "escrita": "OTIMO"
    }
  ],
  "pageable": {
    ...
  }
}
```

### GET /aulas/filtro_data

Busca de aulas que estejam dentro de um intervalo de tempo específico, a data inicial e a data final devem ser passadas no formato de LocalDateTime. Também aceita o id do professor e/ou do aluno como parâmetro, sendo opcionais.

> Exemplo: /aulas/filtro_data?data_inicial=2023-06-01T09:00&data_final=2023-06-30T10:00

O retorno deste endpoint é idêntico ao anterior.

### GET /aulas/{id}

Busca de aulas por id, retorna as informações detalhadas da aula, e as informações resumidas do professor e do aluno.

> Exemplo: /aulas/2
 
```yaml
{
  "id": 2,
  "aluno": {
    "id": 1,
    "nome": "Felipe",
    "telefone": "11 91234-5678",
    "status": "ATIVO"
  },
  "professor": {
    "id": 2,
    "nome": "Larissa",
    "telefone": "11 91234-5678",
    "idioma": "INGLES"
  },
  "livro": {
    "nome": "W2",
    "nivel": "BASICO"
  },
  "licao": "12",
  "data": "2023-06-30T10:00:00",
  "observacao": null,
  "statusAula": "DADA",
  "fala": "OTIMO",
  "audicao": "MUITO_BOM",
  "leitura": "OTIMO",
  "escrita": null
}
```

### POST /aulas

Lança as informações de uma aula, tem como parâmetros obrigatórios o id do aluno, id do professor, nome do livro, lição que foi ou era para ser dada e a data com horário. Os parâmetros opcionais são: observação, status, fala, audição, leitura e escrita. O status padrão é aula DADA.

> Exemplo de corpo de requisição:

```yaml
{
  "aluno": 1,
  "professor": 2,
  "livro": "W2",
  "licao": "16",
  "status": "DADA",
  "data": "2023-07-06T10:00",
  "fala": "REGULAR",
  "audicao": "MUITO_BOM",
  "leitura": "BOM"
}
```

### PUT /aulas/{id}

Rota para alterar as informações de uma aula, os parâmetros que podem ser passados são: lição realizada, observação, status da aula, data da aula e as notas (fala, audição, leitura e escrita).

> Exemplo: /aulas/2

```yaml
{
  "data": "2023-06-30T10:00:00",
  "licao": "Review 2",
  "statusAula": "DADA",
  "escrita": "REGULAR",
  "observacao": "Aluno teve dificuldades na hora de realizar a lição de casa"
}
```

### DELETE /aulas/{id}

Deleta uma aula do sistema através do id, retornando 204 em caso de sucesso.

---
