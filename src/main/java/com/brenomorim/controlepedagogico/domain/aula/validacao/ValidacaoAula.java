package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.aula.DadosAtualizacaoAula;
import com.brenomorim.controlepedagogico.domain.aula.DadosCadastroAula;

public interface ValidacaoAula {

    void validarCadastro(DadosCadastroAula dados);

    void validarAtualizacao(Long id, DadosAtualizacaoAula dados);

}
