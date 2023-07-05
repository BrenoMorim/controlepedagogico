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

Listagem paginada que retorna o nome e o nível dos livros.

### POST /livros

Recebe o nome, idioma, faixa etária e nível de um livro para assim cadastrar um novo registro no banco.

### GET /livros/{nome}

Dado o nome de um livro, retorna suas informações.

### DELETE /livros/{nome}

Deleta o livro pelo nome

---
