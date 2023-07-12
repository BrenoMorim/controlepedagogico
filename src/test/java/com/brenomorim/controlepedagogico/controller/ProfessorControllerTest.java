package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.Idioma;
import com.brenomorim.controlepedagogico.domain.professor.CargoProfessor;
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
import java.util.Arrays;

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

    @Test
    @DisplayName("Professor pode ser cadastrado e posteriormente buscado via GET com id ou via busca avançada")
    @WithMockUser
    void cadastrarProfessor() throws Exception {

        var dadosCadastro = new DadosCadastroProfessor(
                "Professor John", "65175989178", "projessorjohn@email.com", "+55 11 98234-5678", LocalDate.now(),
                Idioma.INGLES, CargoProfessor.INSTRUTOR_II
            );

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroProfessorJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.CREATED.value(), resposta.getStatus());

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        var id = lista.get(lista.size() - 1);
        var dados = new DadosProfessorDetalhado(
                Long.parseLong(id), dadosCadastro.nome(), dadosCadastro.cpf(), dadosCadastro.email(), dadosCadastro.telefone(),
                dadosCadastro.dataAdmissao(), dadosCadastro.idioma(), dadosCadastro.cargo()
        );


        var jsonEsperado = dadosProfessorDetalhadoJson.write(dados).getJson();
        Assertions.assertEquals(jsonEsperado, resposta.getContentAsString());

        var respostaBuscaPorId = mockMvc.perform(MockMvcRequestBuilders.get("/professores/"+id)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        Assertions.assertEquals(jsonEsperado, respostaBuscaPorId.getContentAsString());

        var urlBuscaAvancada = "/professores/busca?nome=%s&email=%s&telefone=%s".formatted(dados.nome(), dados.email(), dados.telefone());
        var respostaBuscaAvancada = mockMvc.perform(MockMvcRequestBuilders.get(urlBuscaAvancada)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        Assertions.assertEquals(jsonEsperado, respostaBuscaAvancada.getContentAsString());

    }

    @Test
    @DisplayName("Status 400 deve ser devolvido caso a busca avançada seja usada sem parâmetros")
    @WithMockUser
    void buscaAvancadaSemParametros() throws Exception {
        var resposta = mockMvc.perform(MockMvcRequestBuilders.get("/professores/busca")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resposta.getStatus());
    }

    @Test
    @DisplayName("Status 400 é retornado caso atualização de cadastro diminua o cargo do professor, somente promoções são aceitas")
    @WithMockUser
    void atualizarCadastro() throws Exception {
        var dadosCadastro = new DadosCadastroProfessor(
                "Novo professor", "10123456789", "novo@email.com", "11 91020-5178", LocalDate.now(),
                Idioma.INGLES, CargoProfessor.INSTRUTOR_I
        );

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroProfessorJson.write(dadosCadastro).getJson())
            ).andReturn().getResponse();

        var lista = Arrays.stream(resposta.getHeader("Location").split("/")).toList();
        var id = lista.get(lista.size() - 1);

        var dadosAtualizacaoInvalidos = new DadosAtualizacaoProfessor(null, null, CargoProfessor.INSTRUTOR_AUXILIAR_II, null);
        var respostaInvalida = mockMvc.perform(MockMvcRequestBuilders.put("/professores/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoProfessorJson.write(dadosAtualizacaoInvalidos).getJson())
            ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaInvalida.getStatus());

        var dadosAtualizacaoValidos = new DadosAtualizacaoProfessor(null, null, CargoProfessor.INSTRUTOR_III, null);
        var respostaValida = mockMvc.perform(MockMvcRequestBuilders.put("/professores/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoProfessorJson.write(dadosAtualizacaoValidos).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), respostaValida.getStatus());

    }

}
