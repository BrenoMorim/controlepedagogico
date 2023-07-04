package com.brenomorim.controlepedagogico.domain.aluno;

import com.brenomorim.controlepedagogico.domain.DadosPessoais;
import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name="alunos")
@Entity(name="Aluno")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private DadosPessoais dadosPessoais;
    private LocalDate dataNascimento;
    private String observacoes;
    @Enumerated(EnumType.STRING)
    private StatusAluno statusAluno;
    @Enumerated(EnumType.STRING)
    private FaixaEtaria faixaEtaria;
    @Enumerated(EnumType.STRING)
    private Nivel nivel;
}
