package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.aluno.Aluno;
import com.brenomorim.controlepedagogico.domain.aluno.AlunoRepository;
import com.brenomorim.controlepedagogico.domain.aula.*;
import com.brenomorim.controlepedagogico.domain.aula.validacao.ValidacaoAula;
import com.brenomorim.controlepedagogico.domain.livro.Livro;
import com.brenomorim.controlepedagogico.domain.livro.LivroRepository;
import com.brenomorim.controlepedagogico.domain.professor.Professor;
import com.brenomorim.controlepedagogico.domain.professor.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("aulas")
public class AulaController {

    @Autowired
    private AulaRepository aulaRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private List<ValidacaoAula> validacoes;

    @GetMapping
    public ResponseEntity<Page<DadosListagemAula>> listar(@PageableDefault(size = 15, sort={"data"}, direction = Sort.Direction.DESC) Pageable paginacao,
              @RequestParam(required = false, name="aluno_id") Long alunoId,
              @RequestParam(required = false, name="professor_id") Long professorId,
              @RequestParam(required = false, name="livro") String livroNome,
              @RequestParam(required = false, name="status") StatusAula status) {

        Aluno aluno = alunoId == null ? null : alunoRepository.getReferenceById(alunoId);
        Professor professor = professorId == null ? null : professorRepository.getReferenceById(professorId);
        Livro livro = livroNome == null ? null : livroRepository.getReferenceById(livroNome);

        var aulaExemplo = new Aula(aluno, livro, professor, status);
        var aulas = aulaRepository.findAll(Example.of(aulaExemplo), paginacao);
        return ResponseEntity.ok(aulas.map(DadosListagemAula::new));
    }

    @GetMapping("/filtro_data")
    @PreAuthorize("hasRole('SECRETARIA') or hasRole('PROFESSOR') or hasRole('COORDENACAO')")
    public ResponseEntity<Page<DadosListagemAula>> filtrarPorData(@PageableDefault(size = 15, sort={"data"}, direction = Sort.Direction.DESC) Pageable paginacao,
                                                                  @RequestParam(name="data_inicial") LocalDateTime dataInicial, @RequestParam(name="data_final") LocalDateTime dataFinal,
                                                                  @RequestParam(required=false, name="professor_id") Long professorId, @RequestParam(required=false, name="aluno_id") Long alunoId) {

        Page<Aula> aulas;

        if (professorId == null && alunoId == null) {
            aulas = aulaRepository.getAulasNoIntervalo(dataInicial, dataFinal, paginacao);
        } else if (professorId == null) {
            aulas = aulaRepository.getAulasNoIntervaloPorAluno(dataInicial, dataFinal, alunoId, paginacao);
        } else if (alunoId == null) {
            aulas = aulaRepository.getAulasNoIntervaloPorProfessor(dataInicial, dataFinal, professorId, paginacao);
        } else {
            aulas = aulaRepository.getAulasNoIntervaloPorAlunoEProfessor(dataInicial, dataFinal, alunoId, professorId, paginacao);
        }

        return ResponseEntity.ok(aulas.map(DadosListagemAula::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosAulaDetalhada> buscaPorId(@PathVariable Long id) {
        var aula = aulaRepository.getReferenceById(id);

        return ResponseEntity.ok(new DadosAulaDetalhada(aula));
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('COORDENACAO')")
    public ResponseEntity<DadosAulaDetalhada> cadastrar(@RequestBody @Valid DadosCadastroAula dados, UriComponentsBuilder uriBuilder) {

        validacoes.forEach(validacaoAula -> validacaoAula.validarCadastro(dados));

        var professor = professorRepository.getReferenceById(dados.professor());
        var aluno = alunoRepository.getReferenceById(dados.aluno());
        var livro = livroRepository.getReferenceById(dados.livro());

        var status = dados.status() == null ? StatusAula.DADA : dados.status();

        var aula = new Aula(null, aluno, professor, livro, dados.licao(), dados.data(), dados.observacao(),
                status, dados.fala(), dados.audicao(), dados.leitura(), dados.escrita());

        aulaRepository.save(aula);
        URI uri = uriBuilder.path("/aulas/{id}").buildAndExpand(aula.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosAulaDetalhada(aula));
    }

    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('COORDENACAO')")
    public ResponseEntity<DadosAulaDetalhada> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoAula dados) {

        validacoes.forEach(validacaoAula -> validacaoAula.validarAtualizacao(id, dados));

        var aula = aulaRepository.getReferenceById(id);
        aula.atualizar(dados);
        return ResponseEntity.ok(new DadosAulaDetalhada(aula));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('PROFESSOR') or hasRole('COORDENACAO')")
    public ResponseEntity deletar(@PathVariable Long id) {
        var aula = aulaRepository.getReferenceById(id);
        aulaRepository.delete(aula);
        return ResponseEntity.noContent().build();
    }

}
