package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.shared.DadosPessoais;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DadosCadastroProfessor(
        @NotNull @Size(min = 5, max = 100)
        String nome,
        @NotNull @Pattern(regexp = "\\d{11}")
        String cpf,
        @NotNull @Email
        String email,
        @NotNull @Pattern(regexp = "^(\\+?\\d{1,3})?[ -]?((\\(\\d{2}\\))|\\d{2})?[ -]?9?\\d{4}[ -]?\\d{4}$")
        String telefone,
        @PastOrPresent
        LocalDate dataAdmissao,
        @NotNull
        Idioma idioma,
        @NotNull
        CargoProfessor cargo) {
    public Professor converter() {
        return new Professor(null, new DadosPessoais(nome, cpf, email, telefone), dataAdmissao == null ? LocalDate.now() : dataAdmissao, idioma, cargo);
    }
}
