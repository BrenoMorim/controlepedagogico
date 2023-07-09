package com.brenomorim.controlepedagogico.domain.professor.validacao;

import com.brenomorim.controlepedagogico.domain.professor.DadosAtualizacaoProfessor;

public interface ValidacaoAtualizacaoProfessor {

    void validar(Long id, DadosAtualizacaoProfessor dados);

}
