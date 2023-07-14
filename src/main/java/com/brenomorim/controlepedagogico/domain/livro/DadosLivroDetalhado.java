package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;

public record DadosLivroDetalhado(
        String nome, Idioma idioma, Nivel nivel, FaixaEtaria faixaEtaria
) {
    public DadosLivroDetalhado(Livro livro) {
        this(livro.getNome(), livro.getIdioma(), livro.getNivel(), livro.getFaixaEtaria());
    }
}
