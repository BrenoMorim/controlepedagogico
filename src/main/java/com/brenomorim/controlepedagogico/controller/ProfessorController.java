package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.Idioma;
import com.brenomorim.controlepedagogico.domain.professor.*;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemProfessor>> listar(@PageableDefault(size = 15, sort={"dadosPessoais.nome"}) Pageable paginacao,
                                                               @RequestParam(required = false, name="idioma") Idioma idioma) {
        Page<Professor> professores;
        if (idioma != null) {
            professores = repository.findAllByIdioma(paginacao, idioma);
        } else {
            professores = repository.findAll(paginacao);
        }
        return ResponseEntity.ok(professores.map(DadosListagemProfessor::new));
    }

    @GetMapping("/busca")
    public ResponseEntity<?> buscaCompleta(@RequestParam(required = false) String nome,
                                                                 @RequestParam(required = false) String telefone, @RequestParam(required = false) String email) {
        if (nome == null && telefone == null && email == null) {
            return ResponseEntity.badRequest().body("Pelo menos um parâmetro de busca deve ser fornecido");
        }
        var professor = repository.buscaAvancada(nome, email, telefone);
        return professor.map(value -> ResponseEntity.ok(new DadosProfessorDetalhado(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosProfessorDetalhado> buscaPorId(@PathVariable Long id) {
        var professor = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosProfessorDetalhado(professor));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosProfessorDetalhado> cadastrar(@RequestBody @Valid DadosCadastroProfessor dados, UriComponentsBuilder uriBuilder) {

        var professorJaExiste = repository.buscaPorEmailTelefoneOuCpf(dados.email(), dados.telefone(), dados.cpf()).size() > 0;
        if (professorJaExiste) throw new EntityExistsException("Já existe um professor cadastrado com essas informações pessoais (telefone, email ou cpf)");

        var professor = dados.converter();
        repository.save(professor);

        URI uri = uriBuilder.path("/professores/{id}").buildAndExpand(professor.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosProfessorDetalhado(professor));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosProfessorDetalhado> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoProfessor dados) {
        var professor = repository.getReferenceById(id);
        professor.atualizar(dados);
        return ResponseEntity.ok(new DadosProfessorDetalhado(professor));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deletar(@PathVariable Long id) {
        var professor = repository.getReferenceById(id);
        repository.delete(professor);
        return ResponseEntity.noContent().build();
    }

}
