package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.Idioma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, String> {
    Page<Livro> findAllByIdioma(Pageable paginacao, Idioma idioma);
}
