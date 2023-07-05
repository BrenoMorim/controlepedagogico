package com.brenomorim.controlepedagogico.domain.aluno;

import java.util.Locale;

public record DadosListagemAluno(Long id, String nome, String telefone, StatusAluno status) {

    public DadosListagemAluno(Aluno aluno) {
        this(aluno.getId(), aluno.getDadosPessoais().getNome(), aluno.getDadosPessoais().getTelefone(), aluno.getStatusAluno());
    }

}
