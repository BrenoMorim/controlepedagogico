package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.Idioma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Professor findProfessorByNomeOrEmailOrTelefone(String nome, String email, String telefone);
    Page<Professor> findAllByIdioma(Pageable paginacao, Idioma idioma);
}
