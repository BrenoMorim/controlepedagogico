package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aluno.AlunoRepository;
import com.brenomorim.controlepedagogico.domain.aula.AulaRepository;
import com.brenomorim.controlepedagogico.domain.aula.DadosAtualizacaoAula;
import com.brenomorim.controlepedagogico.domain.aula.DadosCadastroAula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidacaoAlunoNaoPodeTer2AulasAoMesmoTempo implements ValidacaoAula {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    String mensagem = "Aluno já tem aula salva nesse horário";

    @Override
    public void validarCadastro(DadosCadastroAula dados) {
        var aluno = alunoRepository.getReferenceById(dados.aluno());
        LocalDateTime dataFinal = dados.data().plusHours(1);
        boolean temAula = aulaRepository.getAulasDoAlunoPorDataEHorario(dados.data(), dataFinal, aluno.getId()).size() > 0;

        if (temAula) {
            throw new RegraDeNegocioException(mensagem);
        }
    }

    @Override
    public void validarAtualizacao(Long id, DadosAtualizacaoAula dados) {
        var aluno = aulaRepository.getReferenceById(id).getAluno();

        if (dados.data() == null) {
            return;
        }

        LocalDateTime dataFinal = dados.data().plusHours(1);
        boolean temAula = aulaRepository.getAulasDoAlunoPorDataEHorario(dados.data(), dataFinal, aluno.getId()).size() > 0;

        if (temAula) {
            throw new RegraDeNegocioException(mensagem);
        }
    }
}
