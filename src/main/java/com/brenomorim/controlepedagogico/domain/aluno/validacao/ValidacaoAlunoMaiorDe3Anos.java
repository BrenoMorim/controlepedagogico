package com.brenomorim.controlepedagogico.domain.aluno.validacao;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aluno.DadosCadastroAluno;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;

@Component
public class ValidacaoAlunoMaiorDe3Anos implements ValidacaoCadastroAluno {
    public void validar(DadosCadastroAluno dados) {
        var diferenca = Duration.between(dados.dataNascimento().atStartOfDay(), LocalDate.now().atStartOfDay());
        if (diferenca.toDays() / (365) < 3) {
            throw new RegraDeNegocioException("Aluno precisa ter no minínimo 3 anos de idade para ingressar no curso");
        }
    }
}
