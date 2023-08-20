package br.com.demo.controller;

import static org.mockito.Mockito.doNothing;

import br.com.demo.model.TransferenciaModel;
import br.com.demo.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TransferenciaController.class})
@ExtendWith(SpringExtension.class)
class TransferenciaControllerTest {
    @Autowired
    private TransferenciaController transferenciaController;

    @MockBean
    private TransferenciaService transferenciaService;

    @Test
    void testTransferir() throws Exception {
        doNothing().when(transferenciaService).transferir(Mockito.any());

        TransferenciaModel transferenciaModel = new TransferenciaModel();
        transferenciaModel.setPagador(1);
        transferenciaModel.setRecebedor(1);
        transferenciaModel.setValor(BigDecimal.valueOf(1L));
        String content = (new ObjectMapper()).writeValueAsString(transferenciaModel);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/transferencia/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(transferenciaController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

