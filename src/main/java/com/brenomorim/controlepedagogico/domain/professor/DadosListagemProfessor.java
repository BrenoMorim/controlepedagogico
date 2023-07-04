package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.Idioma;

public record DadosListagemProfessor(Long id, String nome, String telefone, Idioma idioma) {
    public DadosListagemProfessor(Professor professor) {
        this(professor.getId(), professor.getDadosPessoais().getNome(), professor.getDadosPessoais().getTelefone(), professor.getIdioma());
    }
}
