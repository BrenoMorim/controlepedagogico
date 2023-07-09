package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.Idioma;
import com.brenomorim.controlepedagogico.domain.Nivel;
import com.brenomorim.controlepedagogico.domain.livro.DadosCadastroLivro;
import com.brenomorim.controlepedagogico.domain.livro.DadosListagemLivro;
import com.brenomorim.controlepedagogico.domain.livro.DadosLivroDetalhado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroLivro> dadosCadastroLivroJson;
    @Autowired
    private JacksonTester<DadosListagemLivro> dadosListagemLivroJson;
    @Autowired
    private JacksonTester<DadosLivroDetalhado> dadosLivroDetalhadoJson;

    @Test
    @DisplayName("Livro deve ser cadastrado com sucesso quando os dados forem válidos e o livro pode ser pesquisado depois via GET")
    @WithMockUser
    void cadastrarLivro() throws Exception {
        var dados = new DadosLivroDetalhado("W2", Idioma.INGLES, Nivel.BASICO, FaixaEtaria.ADULTS);
        var dadosCadastro = new DadosCadastroLivro(dados.nome(), dados.idioma(), dados.faixaEtaria(), dados.nivel());

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroLivroJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.CREATED.value(), resposta.getStatus());

        var jsonEsperado = dadosLivroDetalhadoJson.write(dados).getJson();
        Assertions.assertEquals(jsonEsperado, resposta.getContentAsString());

        var busca = mockMvc.perform(MockMvcRequestBuilders.get("/livros/W2")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), busca.getStatus());
        Assertions.assertEquals(jsonEsperado, busca.getContentAsString());
    }

    @Test
    @DisplayName("Status 400 deve ser retornado caso dois livros iguais estejam sendo cadastrados")
    @WithMockUser
    void cadastroDuplicado() throws Exception {
        var dadosCadastro = new DadosCadastroLivro("E4", Idioma.ESPANHOL, FaixaEtaria.ADULTS, Nivel.INTERMEDIARIO);

        var respostaInicial = mockMvc.perform(MockMvcRequestBuilders.post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroLivroJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.CREATED.value(), respostaInicial.getStatus());

        var respostaCadastradoDuplicado = mockMvc.perform(MockMvcRequestBuilders.post("/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroLivroJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaCadastradoDuplicado.getStatus());
    }

}
