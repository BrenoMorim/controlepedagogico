package com.brenomorim.controlepedagogico.domain.aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("SELECT a FROM Aluno a WHERE a.statusAluno=ATIVO")
    Page<Aluno> buscaAlunosAtivos(Pageable paginacao);

}
