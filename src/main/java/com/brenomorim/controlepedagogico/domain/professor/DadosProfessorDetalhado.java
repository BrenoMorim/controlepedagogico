package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.shared.Idioma;

import java.time.LocalDate;

public record DadosProfessorDetalhado(Long id, String nome, String cpf, String email, String telefone, LocalDate dataAdmissao,
                                      Idioma idioma, CargoProfessor cargo) {
    public DadosProfessorDetalhado(Professor professor) {
        this(professor.getId(), professor.getDadosPessoais().getNome(), professor.getDadosPessoais().getCpf(),
                professor.getDadosPessoais().getEmail(), professor.getDadosPessoais().getTelefone(), professor.getDataAdmissao(),
                professor.getIdioma(), professor.getCargo());
    }
}
