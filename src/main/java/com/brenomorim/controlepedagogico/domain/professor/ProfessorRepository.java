package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query(value = "SELECT p.* FROM professores p WHERE p.nome LIKE :nome OR p.email LIKE :email OR p.telefone LIKE :telefone ORDER BY p.data_admissao DESC LIMIT 1", nativeQuery = true)
    Optional<Professor> buscaAvancada(String nome, String email, String telefone);
    Page<Professor> findAllByIdioma(Pageable paginacao, Idioma idioma);

    @Query(value = "SELECT p.* FROM professores p WHERE p.email=:email OR p.telefone=:telefone OR p.cpf=:cpf", nativeQuery = true)
    List<Professor> buscaPorEmailTelefoneOuCpf(String email, String telefone, String cpf);

}
