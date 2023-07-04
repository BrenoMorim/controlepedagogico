# Controle Pedagógico

Projeto de sistema para gestão pedagógica, com suporte a CRUD de alunos, professores, aulas, livros e turmas.

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
