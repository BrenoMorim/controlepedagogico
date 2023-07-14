package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;
import com.brenomorim.controlepedagogico.domain.aluno.DadosAtualizacaoAluno;
import com.brenomorim.controlepedagogico.domain.aluno.DadosCadastroAluno;
import com.brenomorim.controlepedagogico.domain.aluno.StatusAluno;
import com.brenomorim.controlepedagogico.domain.aula.DadosAtualizacaoAula;
import com.brenomorim.controlepedagogico.domain.aula.DadosCadastroAula;
import com.brenomorim.controlepedagogico.domain.livro.DadosCadastroLivro;
import com.brenomorim.controlepedagogico.domain.professor.CargoProfessor;
import com.brenomorim.controlepedagogico.domain.professor.DadosCadastroProfessor;
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
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
public class AulaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroAula> dadosCadastroAulaJson;
    @Autowired
    private JacksonTester<DadosAtualizacaoAula> dadosAtualizacaoAulaJson;
    @Autowired
    private JacksonTester<DadosCadastroAluno> dadosCadastroAlunoJson;
    @Autowired
    private JacksonTester<DadosCadastroProfessor> dadosCadastroProfessorJson;
    @Autowired
    private JacksonTester<DadosCadastroLivro> dadosCadastroLivroJson;
    @Autowired
    private JacksonTester<DadosAtualizacaoAluno> dadosAtualizacaoAlunoJson;

    @Test
    @DisplayName("Um aluno pode ter no máximo ter 3 aulas em um único dia")
    @WithMockUser
    void testarMaximoDeAulasPorDia() throws Exception {
        var data = LocalDateTime.now().withHour(8);
        var aluno = criarAluno("7821");
        var professor = criarProfessor("4310", Idioma.INGLES);
        var livro = criarLivro("W2", Idioma.INGLES);

        for (int i = 1; i < 4; i++) {
            var respostaValida = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dadosCadastroAulaJson.write(
                            new DadosCadastroAula(
                                    aluno, professor, livro, "12", data.plusHours(i),
                                    null, null, null, null, null, null)
                    ).getJson())
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.CREATED.value(), respostaValida.getStatus());
        }
        var respostaInvalida = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAulaJson.write(
                        new DadosCadastroAula(
                                aluno, professor, livro, "12", data.plusHours(5),
                                null, null, null, null, null, null)
                ).getJson())
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaInvalida.getStatus());
    }

    @Test
    @DisplayName("Somente alunos com o status ativo podem ter aula")
    @WithMockUser
    void testarAlunoInativosNaoPodemTerAula() throws Exception {
        var data = LocalDateTime.now().withHour(8);
        var aluno = criarAluno("0910");
        var professor = criarProfessor("5623", Idioma.INGLES);
        var livro = criarLivro("W4", Idioma.INGLES);

        inativarAluno(aluno);

        var dados = new DadosCadastroAula(
                aluno, professor, livro, "12", data,
                null, null, null, null, null, null);

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAulaJson.write(dados).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resposta.getStatus());
    }

    @Test
    @DisplayName("Alunos não podem ter duas aulas ao mesmo tempo")
    @WithMockUser
    void testarAlunosNaoPodemTer2AulasAoMesmoTempo() throws Exception {
        var data = LocalDateTime.now().withHour(10);
        var aluno = criarAluno("0912");
        var professor = criarProfessor("5627", Idioma.INGLES);
        var livro = criarLivro("W6", Idioma.INGLES);

        var dados = new DadosCadastroAula(
                aluno, professor, livro, "12", data,
                null, null, null, null, null, null);

        var resposta1 = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAulaJson.write(dados).getJson())
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), resposta1.getStatus());

        var resposta2 = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAulaJson.write(dados).getJson())
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resposta2.getStatus());
    }

    @Test
    @DisplayName("A aula deve ser dada por um professor com o mesmo idioma do livro em que o aluno se encontra")
    @WithMockUser
    void testarProfessoresDevemDarAulaNoIdiomaCorrespondente() throws Exception {
        var data = LocalDateTime.now().withHour(10);
        var aluno = criarAluno("0931");
        var professor = criarProfessor("5664", Idioma.ESPANHOL);
        var livro = criarLivro("W8", Idioma.INGLES);

        var dados = new DadosCadastroAula(
                aluno, professor, livro, "12", data,
                null, null, null, null, null, null);

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAulaJson.write(dados).getJson())
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resposta.getStatus());
    }

    @Test
    @DisplayName("Um aluno pode ter somente 6 aulas por semana")
    @WithMockUser
    void maximo6AulasPorSemanaPorAluno() throws Exception {
        var dataInicial = LocalDateTime.of(2023, 6, 26, 10, 30);
        var aluno = criarAluno("1239");
        var professor = criarProfessor("4078", Idioma.INGLES);
        var livro = criarLivro("W10", Idioma.INGLES);
        for (int i = 0; i < 8; i++) {
            var dados = new DadosCadastroAula(
                    aluno, professor, livro, "12", dataInicial.plusHours(12 * i),
                    null, null, null, null, null, null);

            var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/aulas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(dadosCadastroAulaJson.write(dados).getJson())
            ).andReturn().getResponse();

            var esperado = i == 7 ? HttpStatus.BAD_REQUEST.value() : HttpStatus.CREATED.value();
            Assertions.assertEquals(esperado, resposta.getStatus());
        }
    }

    private Long criarProfessor(String numeroAleatorio, Idioma idioma) throws Exception {
        var dadosCadastro = new DadosCadastroProfessor(
                "Professor", "1234567" + numeroAleatorio, "professor%s@email.com".formatted(numeroAleatorio), "11 91234-" + numeroAleatorio, LocalDate.now(),
                idioma, CargoProfessor.INSTRUTOR_II
        );

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroProfessorJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        return Long.valueOf(lista.get(lista.size() - 1));
    }

    private Long criarAluno(String numeroAleatorio) throws Exception {
        var dadosCadastro = new DadosCadastroAluno("Aluno teste", "11 91234-" + numeroAleatorio, "aluno%s@email.com".formatted(numeroAleatorio),
                "1234567" + numeroAleatorio, LocalDate.now().minusYears(24), FaixaEtaria.ADULTS, Nivel.INTERMEDIARIO, null);

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        return Long.valueOf(lista.get(lista.size() - 1));
    }

    private void inativarAluno(Long id) throws Exception {
        var dados = new DadosAtualizacaoAluno(null, null, null, null, StatusAluno.CURSO_TRANCADO, null);
        mockMvc.perform(MockMvcRequestBuilders.put("/alunos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoAlunoJson.write(dados).getJson())
        );

    }

    private String criarLivro(String nome, Idioma idioma) throws Exception {
        var dados = new DadosCadastroLivro(nome, idioma, FaixaEtaria.ADULTS, Nivel.BASICO);

        mockMvc.perform(MockMvcRequestBuilders.post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroLivroJson.write(dados).getJson())
        ).andReturn().getResponse();
        return dados.nome();
    }

}
