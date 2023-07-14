package com.brenomorim.controlepedagogico.domain.professor.validacao;

import com.brenomorim.controlepedagogico.domain.exception.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.professor.CargoProfessor;
import com.brenomorim.controlepedagogico.domain.professor.DadosAtualizacaoProfessor;
import com.brenomorim.controlepedagogico.domain.professor.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidacaoProfessorSoPodeTerCargoAumentado implements ValidacaoAtualizacaoProfessor {

    @Autowired
    private ProfessorRepository repository;

    @Override
    public void validar(Long id, DadosAtualizacaoProfessor dados) {
        if (dados.cargo() == null)
            return;

        var professor = repository.getReferenceById(id);
        var cargos = List.of(
                CargoProfessor.INSTRUTOR_AUXILIAR_I, CargoProfessor.INSTRUTOR_AUXILIAR_II, CargoProfessor.INSTRUTOR_AUXILIAR_III,
                CargoProfessor.INSTRUTOR_I, CargoProfessor.INSTRUTOR_II, CargoProfessor.INSTRUTOR_III
            );

        var cargoAtual = professor.getCargo();
        var cargoNovo = dados.cargo();

        if (cargos.indexOf(cargoNovo) < cargos.indexOf(cargoAtual)) {
            throw new RegraDeNegocioException("Professor não pode ter o cargo diminuído, somente ser promovido ao ter seu cadastro atualizado");
        }
    }
}
