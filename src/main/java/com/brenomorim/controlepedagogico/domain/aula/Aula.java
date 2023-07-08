package com.brenomorim.controlepedagogico.domain.aula;

import com.brenomorim.controlepedagogico.domain.aluno.Aluno;
import com.brenomorim.controlepedagogico.domain.livro.Livro;
import com.brenomorim.controlepedagogico.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@Table(name="aulas")
@Entity(name="Aula")
public class Aula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="aluno_id")
    private Aluno aluno;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="professor_id")
    private Professor professor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="livro_nome")
    private Livro livro;
    private String licao;
    private LocalDateTime data;
    private String observacao;
    @Enumerated(EnumType.STRING)
    private StatusAula status;
    @Enumerated(EnumType.STRING)
    private Conceito fala;
    @Enumerated(EnumType.STRING)
    private Conceito audicao;
    @Enumerated(EnumType.STRING)
    private Conceito leitura;
    @Enumerated(EnumType.STRING)
    private Conceito escrita;

    public Aula(Aluno aluno, Livro livro, Professor professor, StatusAula status) {
        this.aluno = aluno;
        this.livro = livro;
        this.professor = professor;
        this.status = status;
    }

    public void atualizar(DadosAtualizacaoAula dados) {
        if (dados.licao() != null) this.licao = dados.licao();
        if (dados.observacao() != null) this.observacao = dados.observacao();
        if (dados.status() != null) this.status = dados.status();
        if (dados.data() != null) this.data = dados.data();
        if (dados.fala() != null) this.fala = dados.fala();
        if (dados.audicao() != null) this.audicao = dados.audicao();
        if (dados.leitura() != null) this.leitura = dados.leitura();
        if (dados.escrita() != null) this.escrita = dados.escrita();
    }

}
