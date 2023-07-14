package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.exception.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aluno.AlunoRepository;
import com.brenomorim.controlepedagogico.domain.aula.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ValidacaoMaximo3AulasPorDia implements ValidacaoAula {

    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public void validarCadastro(DadosCadastroAula dados) {
        var aluno = alunoRepository.getReferenceById(dados.aluno());
        var aulas = getAulasDadas(dados.data(), aluno.getId());

        if (aulas.size() >= 3) {
            throw new RegraDeNegocioException("Aluno pode realizar no máximo 3 aulas por dia");
        }
    }

    @Override
    public void validarAtualizacao(Long id, DadosAtualizacaoAula dados) {
        var aluno = aulaRepository.getReferenceById(id).getAluno();
        if (dados.status() != StatusAula.DADA || dados.data() == null) {
            return;
        }
        var aulas = getAulasDadas(dados.data(), aluno.getId());

        if (aulas.size() >= 3) {
            throw new RegraDeNegocioException("Aluno pode realizar no máximo 3 aulas por dia");
        }
    }

    private List<Aula> getAulasDadas(LocalDateTime data, Long alunoId) {

        var dataInicial = data.minusHours(data.getHour());
        var dataFinal = data.plusHours(24 - data.getHour());

        return aulaRepository
                .getAulasDoAlunoPorDataEHorario(dataInicial, dataFinal, alunoId)
                .stream().filter(aula -> aula.getStatus() == StatusAula.DADA).toList();
    }

}
