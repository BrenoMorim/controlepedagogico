package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.Idioma;
import com.brenomorim.controlepedagogico.domain.livro.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping("livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemLivro>> listar(@PageableDefault(size=10, sort={"nome"}) Pageable paginacao,
                                                           @RequestParam(required = false, name = "idioma") Idioma idioma) {
        Page<DadosListagemLivro> livros;
        if (idioma != null) {
            livros = livroRepository.findAllByIdioma(paginacao, idioma).map(DadosListagemLivro::new);
        } else {
            livros = livroRepository.findAll(paginacao).map(DadosListagemLivro::new);
        }
        return ResponseEntity.ok(livros);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosLivroDetalhado> cadastrar(@RequestBody @Valid DadosCadastroLivro dados, UriComponentsBuilder uriBuilder) {
        var livro = new Livro(dados.nome(), dados.idioma(), dados.faixaEtaria(), dados.nivel());
        livroRepository.save(livro);

        URI uri = uriBuilder.path("/livros/{nome}").buildAndExpand(livro.getNome()).toUri();
        return ResponseEntity.created(uri).body(new DadosLivroDetalhado(livro));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<DadosLivroDetalhado> detalhar(@PathVariable String nome) {
        var livro = livroRepository.getReferenceById(nome);
        return ResponseEntity.ok(new DadosLivroDetalhado(livro));
    }

    @DeleteMapping("/{nome}")
    @Transactional
    public ResponseEntity deletar(@PathVariable String nome) {
        var livro = livroRepository.getReferenceById(nome);
        livroRepository.delete(livro);

        return ResponseEntity.noContent().build();
    }

}
