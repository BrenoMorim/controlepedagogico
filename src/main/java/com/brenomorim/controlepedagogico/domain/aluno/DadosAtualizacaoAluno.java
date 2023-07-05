package com.brenomorim.controlepedagogico.domain.aluno;

import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoAluno(
        @Email String email,
        @Pattern(regexp = "^(\\+?\\d{1,3})?[ -]?((\\(\\d{2}\\))|\\d{2})?[ -]?9?\\d{4}[ -]?\\d{4}$") String telefone,
        String observacoes,
        FaixaEtaria faixaEtaria,
        StatusAluno statusAluno,
        Nivel nivel
) {
}
