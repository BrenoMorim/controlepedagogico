package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.exception.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aluno.AlunoRepository;
import com.brenomorim.controlepedagogico.domain.aula.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidacaoMaximo6AulasDadasPorSemana implements ValidacaoAula {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    private String mensagem = "Aluno pode realizar no máximo 6 aulas por semana";

    @Override
    public void validarCadastro(DadosCadastroAula dados) {
        var aluno = alunoRepository.getReferenceById(dados.aluno());
        var aulas = getAulasDadas(dados.data(), aluno.getId());

        if (aulas.size() > 6) {
            throw new RegraDeNegocioException(mensagem);
        }
    }

    @Override
    public void validarAtualizacao(Long id, DadosAtualizacaoAula dados) {
        var aluno = aulaRepository.getReferenceById(id).getAluno();
        if (dados.status() != StatusAula.DADA || dados.data() == null) {
            return;
        }
        var aulas = getAulasDadas(dados.data(), aluno.getId());

        if (aulas.size() > 6) {
            throw new RegraDeNegocioException(mensagem);
        }
    }

    private List<Aula> getAulasDadas(LocalDateTime data, Long alunoId) {
        var diaDaSemana = data.getDayOfWeek().getValue();
        var dataInicial = data.minusDays(diaDaSemana);
        var dataFinal = data.plusDays(7 - diaDaSemana);

        return aulaRepository
                .getAulasDoAlunoPorDataEHorario(dataInicial, dataFinal, alunoId)
                .stream().filter(aula -> aula.getStatus() == StatusAula.DADA).toList();
    }
}
