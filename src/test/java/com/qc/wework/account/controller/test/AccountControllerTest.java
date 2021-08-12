package com.qc.wework.account.controller.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String loginParam() {
        return "{\n" +
                "    \"mobile\": \"18601690499\",\n" +
                "    \"password\": \"123\"\n" +
                "}";
    }

    @Test
    public void testLogin() throws Exception {

        ResultActions actions = mockMvc.perform(post("/api/qc/login").content(loginParam()).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testListAccounts() throws Exception {

        ResultActions actions = mockMvc.perform(get("/api/qc/wework/account/list")
                .header("token", "31961c80-558c-44f8-bc07-264d793a664b")
                .param("pageNum", "1")
                .param("pageSize", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }
}


