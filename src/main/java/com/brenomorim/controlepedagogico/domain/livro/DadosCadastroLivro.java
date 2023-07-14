package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroLivro(
        @NotNull @Size(min=2, max = 20)
        String nome,
        @NotNull Idioma idioma,
        @NotNull FaixaEtaria faixaEtaria,
        @NotNull Nivel nivel
) {
}
