package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.Idioma;

import java.time.LocalDate;

public record DadosCadastroProfessor(String nome, String cpf, String email, String telefone,
                                     LocalDate dataAdmissao, Idioma idioma, CargoProfessor cargo) {
}
