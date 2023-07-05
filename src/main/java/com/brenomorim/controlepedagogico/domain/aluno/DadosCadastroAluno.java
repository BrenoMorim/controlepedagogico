package com.brenomorim.controlepedagogico.domain.aluno;

import com.brenomorim.controlepedagogico.domain.DadosPessoais;
import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosCadastroAluno(
        @NotNull @Size(min = 5, max = 100)
        String nome,
        @NotNull @Pattern(regexp = "^(\\+?\\d{1,3})?[ -]?((\\(\\d{2}\\))|\\d{2})?[ -]?9?\\d{4}[ -]?\\d{4}$")
        String telefone,
        @NotNull @Email
        String email,
        @NotNull @Pattern(regexp = "^\\d{11}$")
        String cpf,
        @NotNull @Past
        LocalDate dataNascimento,
        @NotNull
        FaixaEtaria faixaEtaria,
        Nivel nivel,
        String observacoes) {

        public Aluno converter() {
                var dadosPessoais = new DadosPessoais(nome, cpf, email, telefone);
                return new Aluno(null, dadosPessoais, dataNascimento, observacoes, StatusAluno.ATIVO, faixaEtaria, nivel == null ? Nivel.BASICO : nivel);
        }

}
