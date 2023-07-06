package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aluno.AlunoRepository;
import com.brenomorim.controlepedagogico.domain.aluno.StatusAluno;
import com.brenomorim.controlepedagogico.domain.aula.AulaRepository;
import com.brenomorim.controlepedagogico.domain.aula.DadosAtualizacaoAula;
import com.brenomorim.controlepedagogico.domain.aula.DadosCadastroAula;
import com.brenomorim.controlepedagogico.domain.aula.StatusAula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoSomenteAlunosAtivosPodemTerAula  implements ValidacaoAula {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private AulaRepository aulaRepository;
    private String mensagem = "Somente alunos ativos podem ter aula, o status deste aluno é: ";

    @Override
    public void validarCadastro(DadosCadastroAula dados) {
        var aluno = alunoRepository.getReferenceById(dados.aluno());
        if (aluno.getStatusAluno() != StatusAluno.ATIVO) {
            throw new RegraDeNegocioException(mensagem + aluno.getStatusAluno());
        }
    }

    @Override
    public void validarAtualizacao(Long id, DadosAtualizacaoAula dados) {
        var aluno = aulaRepository.getReferenceById(id).getAluno();
        if (dados.status() == StatusAula.DADA && aluno.getStatusAluno() != StatusAluno.ATIVO) {
            throw new RegraDeNegocioException(mensagem + aluno.getStatusAluno());
        }
    }
}
