package com.brenomorim.controlepedagogico.domain.aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("SELECT a FROM Aluno a WHERE a.statusAluno=ATIVO")
    Page<Aluno> buscaAlunosAtivos(Pageable paginacao);

    @Query(value = "SELECT a.* FROM alunos a WHERE a.email=:email OR a.telefone=:telefone OR a.cpf=:cpf", nativeQuery = true)
    List<Aluno> buscaPorEmailTelefoneOuCpf(String email, String telefone, String cpf);

}
