package com.brenomorim.controlepedagogico.domain.aluno;

import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;

import java.time.LocalDate;

public record DadosAlunoDetalhado(String nome, String telefone, String email, String cpf, LocalDate dataNascimento, String observacoes,
                                  StatusAluno statusAluno, FaixaEtaria faixaEtaria, Nivel nivel) {
    public DadosAlunoDetalhado(Aluno aluno) {
        this(aluno.getDadosPessoais().getNome(), aluno.getDadosPessoais().getTelefone(), aluno.getDadosPessoais().getEmail(),
                aluno.getDadosPessoais().getCpf(), aluno.getDataNascimento(), aluno.getObservacoes(), aluno.getStatusAluno(),
                aluno.getFaixaEtaria(), aluno.getNivel());
    }

}
