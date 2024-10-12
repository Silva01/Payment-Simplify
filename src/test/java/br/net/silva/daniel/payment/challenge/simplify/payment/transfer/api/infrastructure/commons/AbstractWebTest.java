package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public abstract class AbstractWebTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ResultActions postRequest(String url, Object request) throws Exception {
        return mockMvc.perform(post(url)
                                       .contentType("application/json")
                                       .content(objectMapper.writeValueAsString(request)));
    }
}
