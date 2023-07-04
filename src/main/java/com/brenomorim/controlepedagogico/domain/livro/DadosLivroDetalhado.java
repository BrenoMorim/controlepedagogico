package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Idioma;
import com.brenomorim.controlepedagogico.domain.Nivel;

public record DadosLivroDetalhado(
        String nome, Idioma idioma, Nivel nivel, FaixaEtaria faixaEtaria
) {
    public DadosLivroDetalhado(Livro livro) {
        this(livro.getNome(), livro.getIdioma(), livro.getNivel(), livro.getFaixaEtaria());
    }
}
