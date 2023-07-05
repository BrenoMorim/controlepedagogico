package com.brenomorim.controlepedagogico.domain.aula;

import java.time.LocalDateTime;

public record DadosListagemAula(Long id, String aluno, String professor, String livro, String licao,
                                LocalDateTime data, StatusAula statusAula, Conceito fala, Conceito audicao, Conceito leitura, Conceito escrita) {

    public DadosListagemAula(Aula aula) {
        this(aula.getId(), aula.getAluno().getDadosPessoais().getNome(), aula.getProfessor().getDadosPessoais().getNome(), aula.getLivro().getNome(),
                aula.getLicao(), aula.getData(), aula.getStatus(), aula.getFala(), aula.getAudicao(), aula.getLeitura(), aula.getEscrita());
    }

}
