package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;
import com.brenomorim.controlepedagogico.domain.aluno.DadosAlunoDetalhado;
import com.brenomorim.controlepedagogico.domain.aluno.DadosAtualizacaoAluno;
import com.brenomorim.controlepedagogico.domain.aluno.DadosCadastroAluno;
import com.brenomorim.controlepedagogico.domain.aluno.StatusAluno;
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
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroAluno> dadosCadastroAlunoJson;
    @Autowired
    private JacksonTester<DadosAtualizacaoAluno> dadosAtualizacaoAlunoJson;
    @Autowired
    private JacksonTester<DadosAlunoDetalhado> dadosAlunoDetalhadoJson;

    @Test
    @DisplayName("Alunos podem ser cadastrados, somente caso ainda não exista um registro com as mesmas informações pessoais")
    @WithMockUser
    void cadastrarAluno() throws Exception {

        var dadosCadastro = new DadosCadastroAluno("Aluno teste", "11 91234-5678", "aluno@email.com",
                "12345678910", LocalDate.now().minusYears(24), FaixaEtaria.ADULTS, Nivel.INTERMEDIARIO, null);

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        var id = lista.get(lista.size() - 1);
        var dados = new DadosAlunoDetalhado(Long.parseLong(id), dadosCadastro.nome(), dadosCadastro.telefone(), dadosCadastro.email(), dadosCadastro.cpf(),
                dadosCadastro.dataNascimento(), dadosCadastro.observacoes(), StatusAluno.ATIVO, dadosCadastro.faixaEtaria(), dadosCadastro.nivel()
        );

        Assertions.assertEquals(HttpStatus.CREATED.value(), resposta.getStatus());

        var jsonEsperado = dadosAlunoDetalhadoJson.write(dados).getJson();
        Assertions.assertEquals(jsonEsperado, resposta.getContentAsString());

        var respostaDuplicada = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaDuplicada.getStatus());
    }

    @Test
    @DisplayName("Status 400 deve ser retornado caso o aluno tenha menos de 3 anos ou os dados estejam em formato inválido")
    @WithMockUser
    void cadastroComDadosInvalidos() throws Exception {
        var dadosCadastroMenorDe3Anos = new DadosCadastroAluno(
                "Aluno teste", "11 95678-1234", "aluno@teste.com", "10123456789",
                LocalDate.now().minusYears(2), FaixaEtaria.TODDLER, null, null
        );

        var respostaMenorDe3Anos = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dadosCadastroMenorDe3Anos).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaMenorDe3Anos.getStatus());

        var dadosCadastroInvalido = new DadosCadastroAluno(
                "Aluno teste", "invalido", "invalido", "123",
                LocalDate.of(2000, 10, 14), FaixaEtaria.ADULTS, null, null
        );

        var respostaCadastroInvalido = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dadosCadastroInvalido).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaCadastroInvalido.getStatus());
    }

    @Test
    @DisplayName("Dados do aluno podem ser atualizados, retornando status 200")
    @WithMockUser
    void dadosDoAlunoPodemSerAtualizados() throws Exception {
        var dados = new DadosCadastroAluno(
                "Aluno teste", "11 95678-6654", "alunonovo@teste.com", "10113456789",
                LocalDate.now().minusYears(15), FaixaEtaria.TEENS, null, null
        );

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroAlunoJson.write(dados).getJson())
        ).andReturn().getResponse();

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        var id = lista.get(lista.size() - 1);
        var dadosAtualizacao = new DadosAtualizacaoAluno(null, null, null, null, StatusAluno.CURSO_TRANCADO, Nivel.INTERMEDIARIO);
        var respostaAtualizacao = mockMvc.perform(MockMvcRequestBuilders.put("/alunos/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoAlunoJson.write(dadosAtualizacao).getJson())
        ).andReturn().getResponse();

        var jsonEsperado = new DadosAlunoDetalhado(
                Long.parseLong(id), dados.nome(), dados.telefone(), dados.email(), dados.cpf(), dados.dataNascimento(),
                dados.observacoes(), dadosAtualizacao.statusAluno(), dados.faixaEtaria(), dadosAtualizacao.nivel()
            );

        Assertions.assertEquals(HttpStatus.OK.value(), respostaAtualizacao.getStatus());
        Assertions.assertEquals(respostaAtualizacao.getContentAsString(), dadosAlunoDetalhadoJson.write(jsonEsperado).getJson());
    }

}
