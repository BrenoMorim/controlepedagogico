package com.brenomorim.controlepedagogico.domain.aula;

import com.brenomorim.controlepedagogico.domain.aluno.DadosListagemAluno;
import com.brenomorim.controlepedagogico.domain.livro.DadosListagemLivro;
import com.brenomorim.controlepedagogico.domain.professor.DadosListagemProfessor;

import java.time.LocalDateTime;

public record DadosAulaDetalhada(Long id, DadosListagemAluno aluno, DadosListagemProfessor professor, DadosListagemLivro livro, String licao, LocalDateTime data,
                                 String observacao, StatusAula statusAula, Conceito fala, Conceito audicao, Conceito leitura, Conceito escrita) {

    public DadosAulaDetalhada(Aula aula) {
        this(aula.getId(), new DadosListagemAluno(aula.getAluno()), new DadosListagemProfessor(aula.getProfessor()), new DadosListagemLivro(aula.getLivro()),
                aula.getLicao(), aula.getData(), aula.getObservacao(), aula.getStatus(), aula.getFala(), aula.getAudicao(), aula.getLeitura(), aula.getEscrita());
    }

}
