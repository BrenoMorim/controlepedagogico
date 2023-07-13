package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.DadosPessoais;
import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;
import com.brenomorim.controlepedagogico.domain.aluno.*;
import com.brenomorim.controlepedagogico.domain.aluno.validacao.ValidacaoCadastroAluno;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;
    @Autowired
    private List<ValidacaoCadastroAluno> validacoes;

    @GetMapping
    public ResponseEntity<Page<DadosListagemAluno>> listar(@PageableDefault(size = 15, sort={"nivel"}) Pageable paginacao,
                                                           @RequestParam(required = false) String nome,
                                                           @RequestParam(required = false, name = "status") StatusAluno status,
                                                           @RequestParam(required = false) FaixaEtaria faixaEtaria,
                                                           @RequestParam(required = false) Nivel nivel) {
        Page<Aluno> alunos;
        if (nome == null && status == null && faixaEtaria == null && nivel == null) {
            alunos = repository.buscaAlunosAtivos(paginacao);
        } else {
            var alunoExemplo = new Aluno(null, new DadosPessoais(nome, null, null, null), null, null, status, faixaEtaria, nivel);
            alunos = repository.findAll(Example.of(alunoExemplo), paginacao);
        }
        return ResponseEntity.ok(alunos.map(DadosListagemAluno::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosAlunoDetalhado> buscarPorId(@PathVariable Long id) {
        var aluno = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosAlunoDetalhado(aluno));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosAlunoDetalhado> cadastrar(@RequestBody @Valid DadosCadastroAluno dados, UriComponentsBuilder uriBuilder) {

        validacoes.forEach(validacao -> validacao.validar(dados));

        var alunoJaExiste = repository.buscaPorEmailTelefoneOuCpf(dados.email(), dados.telefone(), dados.cpf()).size() > 0;
        if (alunoJaExiste) throw new EntityExistsException("Já existe um aluno cadastrado com essas informações pessoais (telefone, email ou cpf)");

        var aluno = dados.converter();
        repository.save(aluno);

        URI uri = uriBuilder.path("/alunos/{id}").buildAndExpand(aluno.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosAlunoDetalhado(aluno));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosAlunoDetalhado> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoAluno dados) {
        var aluno = repository.getReferenceById(id);
        aluno.atualizar(dados);
        return ResponseEntity.ok(new DadosAlunoDetalhado(aluno));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletar(@PathVariable Long id) {
        var aluno = repository.getReferenceById(id);
        repository.delete(aluno);
        return ResponseEntity.noContent().build();
    }

}
