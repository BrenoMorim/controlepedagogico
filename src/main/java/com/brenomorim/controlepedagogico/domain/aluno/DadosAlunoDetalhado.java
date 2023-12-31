package com.brenomorim.controlepedagogico.domain.aluno;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;

import java.time.LocalDate;

public record DadosAlunoDetalhado(Long id, String nome, String telefone, String email, String cpf, LocalDate dataNascimento, String observacoes,
                                  StatusAluno statusAluno, FaixaEtaria faixaEtaria, Nivel nivel) {
    public DadosAlunoDetalhado(Aluno aluno) {
        this(aluno.getId(), aluno.getDadosPessoais().getNome(), aluno.getDadosPessoais().getTelefone(), aluno.getDadosPessoais().getEmail(),
                aluno.getDadosPessoais().getCpf(), aluno.getDataNascimento(), aluno.getObservacoes(), aluno.getStatusAluno(),
                aluno.getFaixaEtaria(), aluno.getNivel());
    }

}
