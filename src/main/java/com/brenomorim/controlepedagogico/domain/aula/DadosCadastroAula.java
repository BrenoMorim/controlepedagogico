package com.brenomorim.controlepedagogico.domain.aula;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosCadastroAula(

        @NotNull
        Long aluno,
        @NotNull
        Long professor,
        @NotNull
        String livro,
        @NotNull
        String licao,
        @NotNull
        LocalDateTime data,
        String observacao,
        StatusAula status,
        Conceito fala,
        Conceito audicao,
        Conceito leitura,
        Conceito escrita
) {
}
