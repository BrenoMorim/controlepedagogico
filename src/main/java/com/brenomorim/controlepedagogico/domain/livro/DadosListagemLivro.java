package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.Nivel;

public record DadosListagemLivro(
        String nome, Nivel nivel
) {
    public DadosListagemLivro(Livro livro) {
        this(livro.getNome(), livro.getNivel());
    }
}
