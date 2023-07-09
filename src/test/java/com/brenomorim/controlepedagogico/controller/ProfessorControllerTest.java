package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Nivel;
import com.brenomorim.controlepedagogico.domain.aluno.DadosAlunoDetalhado;
import com.brenomorim.controlepedagogico.domain.aluno.DadosAtualizacaoAluno;
import com.brenomorim.controlepedagogico.domain.aluno.DadosCadastroAluno;
import com.brenomorim.controlepedagogico.domain.aluno.StatusAluno;
import com.brenomorim.controlepedagogico.domain.professor.DadosAtualizacaoProfessor;
import com.brenomorim.controlepedagogico.domain.professor.DadosCadastroProfessor;
import com.brenomorim.controlepedagogico.domain.professor.DadosProfessorDetalhado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroProfessor> dadosCadastroProfessorJson;
    @Autowired
    private JacksonTester<DadosAtualizacaoProfessor> dadosAtualizacaoProfessorJson;
    @Autowired
    private JacksonTester<DadosProfessorDetalhado> dadosProfessorDetalhadoJson;

}
