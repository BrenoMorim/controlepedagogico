package com.brenomorim.controlepedagogico.domain.aula.validacao;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.aula.DadosAtualizacaoAula;
import com.brenomorim.controlepedagogico.domain.aula.DadosCadastroAula;
import com.brenomorim.controlepedagogico.domain.livro.LivroRepository;
import com.brenomorim.controlepedagogico.domain.professor.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoIdiomaDoProfessorDeveSerIgualDoLivro implements ValidacaoAula {

    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private LivroRepository livroRepository;

    @Override
    public void validarCadastro(DadosCadastroAula dados) {
        var professor = professorRepository.getReferenceById(dados.professor());
        var livro = livroRepository.getReferenceById(dados.livro());

        if (professor.getIdioma() != livro.getIdioma()) {
            throw new RegraDeNegocioException("Um professor de %s não pode dar aula de %s".formatted(professor.getIdioma(), livro.getIdioma()));
        }
    }

    @Override
    public void validarAtualizacao(Long id, DadosAtualizacaoAula dados) {
    }
}
