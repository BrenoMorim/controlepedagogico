CREATE TABLE IF NOT EXISTS alunos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    observacoes VARCHAR(255),
    data_nascimento DATE NOT NULL,
    status_aluno VARCHAR(20) NOT NULL,
    faixa_etaria VARCHAR(20) NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS professores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    data_admissao DATE NOT NULL,
    idioma VARCHAR(20) NOT NULL,
    cargo VARCHAR(20) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS livros (
    nome VARCHAR(20) NOT NULL,
    idioma VARCHAR(20) NOT NULL,
    faixa_etaria VARCHAR(20) NOT NULL,
    nivel VARCHAR(20) NOT NULL,
    PRIMARY KEY(nome)
);

CREATE TABLE IF NOT EXISTS aulas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    aluno_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    livro_nome VARCHAR(20) NOT NULL,
    licao VARCHAR(10) NOT NULL
    data DATETIME NOT NULL,
    observacao VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    fala VARCHAR(10),
    audicao VARCHAR(10),
    leitura VARCHAR(10),
    escrita VARCHAR(10),
    PRIMARY KEY(id),
    CONSTRAINT fk_aulas_aluno_id FOREIGN KEY(aluno_id) REFERENCES alunos(id),
    CONSTRAINT fk_aulas_professor_id FOREIGN KEY(professor_id) REFERENCES professores(id),
    CONSTRAINT fk_aulas_livro_nome FOREIGN KEY(livro_nome) REFERENCES livros(nome)
);
