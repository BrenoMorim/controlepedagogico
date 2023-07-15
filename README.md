# Controle Pedagógico

Projeto de sistema para gestão pedagógica, com suporte a CRUD de alunos, professores, aulas, livros e turmas.

| :placard: Vitrine.Dev |                                            |
|-----------------------|--------------------------------------------|
| :sparkles: Nome       | **Controle Pedagógico**                    |
| :label: Tecnologias   | Java, Spring, MySQL, FlyWay                |

![Resposta de exemplo do endpoint /aulas](https://github.com/BrenoMorim/controlepedagogico/blob/main/imagem-do-projeto.png?raw=true#vitrinedev)

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
- INSTRUTOR_III

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

Roles:

- ROLE_PROFESSOR
- ROLE_ALUNO
- ROLE_SECRETARIA
- ROLE_COORDENACAO

## Regras de negócio

Até o momento, a aplicação contém as seguintes regras de negócio: 
- somente alunos com status ativo podem ter aula;
- um aluno pode realizar no máximo 6 aulas por semana;
- aluno pode realizar no máximo 3 aulas por dia;
- alunos só podem ser matriculados a partir de 3 anos de idade;
- alunos não podem realizar mais de uma aula ao mesmo tempo;
- professor não pode ter seu cargo diminuído, somente promoções são aceitas.

## Importando e preparando o projeto

```sh
git clone https://github.com/BrenoMorim/controlepedagogico.git controlepedagogico
cd controlepedagogico
./mvnw clean install
```

Com esses comandos, o projeto e todas as suas dependências já estarão carregadas, também é possível fazer a mesma coisa com uma IDE como IntelliJ ou Eclipse.

## Executando o projeto

```sh
./mvnw spring-boot:run
```

## Rodando os testes automatizados

```sh
./mvnw test
```

## Execuntando com o Docker

Para executar o projeto com Docker, algumas variáveis de ambiente devem ser declaradas em um arquivo .env na raiz do projeto, segue um exemplo com as configurações:

```properties
### Arquivo .env ###

### Configuração da base de dados ###

DATASOURCE_URL=jdbc:mysql://mysqldb:3306/controle_pedagogico
DATASOURCE_DATABASE=controle_pedagogico
DATASOURCE_USERNAME=root
DATASOURCE_PASSWORD=root

### Configuração das portas ###

MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
```

Com as variáveis de ambiente já configuradas, basta executar o seguinte comando:

```sh
docker compose up
```

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
  "id": 3,
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

## Autenticação

Os endpoints de autenticação são os seguintes:

### POST /auth/login

Realiza login, retornando token JWT para ser usado como Authorization:

> Exemplo:

```yml
{
  "email": "breno@email.com",
  "senha": "1234567"
}
```

```yml
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicmVub0BlbWFpbC5jb20iLCJpYXQiOjE2ODkyODM3MjcsImV4cCI6MTY4OTI4NTE2N30.EImlkVfTUdZNZVgpqe-doi4P7hSp9v47C59NLbHJiTo"
}
```

### POST /auth/cadastro

Realiza cadastro, retornando os dados do usuário cadastrado:

> Exemplo:

```yml
{
  "email": "secretaria@email.com",
  "senha": "1234567",
  "role": "ROLE_SECRETARIA"
}
```

```yml
{
  "id": 1,
  "email": "secretaria@email.com",
  "role": "ROLE_SECRETARIA"
}
```

### GET /auth/usuarios

Busca pagina pelos usuários, pode ser passado o atributo role como parâmetro de URL.

```yml
{
  "content": [
    {
      "id": 6,
      "email": "aluno@email.com",
      "role": "ROLE_ALUNO"
    },
    {
      "id": 7,
      "email": "coordenacao@email.com",
      "role": "ROLE_COORDENACAO"
    }
  ],
  "pageable": {
    ...
  }
}
```

### /auth/usuarios/aluno@email.com

Busca usuário pelo email, retornando os mesmos dados do endpoint anterior.

```yml
{
  "id": 6,
  "email": "aluno@email.com",
  "role": "ROLE_ALUNO"
}
```

### DELETE /auth/usuarios/aluno@email.com

Deleta um usuário pelo seu email.

## Regras de autorização

> Aluno Controller

- GET autorizado para professores, secretaria e coordenação
- Operações POST, PUT e DELETE autorizado para a secretaria e coordenação somente

> Aula Controller

- GET autorizado para todas as roles, inclusive alunos
- POST, PUT e DELETE autorizados somente para professores e a coordenação

> Livro Controller

- GET público até para usuários não autenticados
- POST e DELETE permitido somente para a coordenação

> Professor Controller

- GET autorizado para professores, secretaria e coordenação
- Operações POST, PUT e DELETE autorizado para a coordenação somente

> Auth Controller

- /auth/login público
- /auth/cadastro permitidos somente para a coordenação e secretaria
- No caso de cadastro feito pela secretaria, o usuário cadastrado só pode ser da role aluno
- GET relacionado a /auth/usuarios é permitido para coordenação, secretaria e professores
- DELETE permitido somente para a coordenação

---
