package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.shared.DadosPessoais;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Table(name="professores")
@Entity(name="Professor")
public class Professor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private DadosPessoais dadosPessoais;
    private LocalDate dataAdmissao;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    @Enumerated(EnumType.STRING)
    private CargoProfessor cargo;

    public void atualizar(DadosAtualizacaoProfessor dados) {
        if (dados.email() != null || dados.telefone() != null) {
            var novoEmail = dados.email() == null ? dadosPessoais.getEmail() : dados.email();
            var novoTelefone = dados.telefone() == null ? dadosPessoais.getTelefone() : dados.telefone();
            this.dadosPessoais = new DadosPessoais(dadosPessoais.getNome(), dadosPessoais.getCpf(), novoEmail, novoTelefone);
        }
        if (dados.cargo() != null) {
            this.cargo = dados.cargo();
        }
        if (dados.idioma() != null) {
            this.idioma = dados.idioma();
        }
    }

}
