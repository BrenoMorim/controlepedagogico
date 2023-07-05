package com.brenomorim.controlepedagogico.domain.aula;

import java.time.LocalDateTime;

public record DadosAtualizacaoAula(
        String licao,
        LocalDateTime data,
        String observacao,
        StatusAula status,
        Conceito fala,
        Conceito audicao,
        Conceito leitura,
        Conceito escrita
) {
}
