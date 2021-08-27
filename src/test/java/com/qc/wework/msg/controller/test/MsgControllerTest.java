package com.qc.wework.msg.controller.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
public class MsgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String token = "9a82c91d-6192-4005-b21f-4fdac733d58b";
    private static final String current = "0";
    private static final String pageSize = "10";

    String roomId = "wrxyF2EAAADm8TwetrSSoqdWZCe8OFSA";

    private String loginParam() {
        return "{\n" +
                "    \"mobile\": \"18601690499\",\n" +
                "    \"password\": \"123\"\n" +
                "}";
    }

//    @Test
//    public void testLogin() throws Exception {
//
//        ResultActions actions = mockMvc.perform(post("/api/qc/login").content(loginParam()).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
//        System.out.println(actions.andReturn().getResponse().getContentAsString());
//    }

    @Test
    public void testListMsgRooms() throws Exception {

        ResultActions actions = mockMvc.perform(get("/api/qc/wework/msg/room/"+roomId+"/content")
                .header("token", token)
                .param("current", current)
                .param("pageSize", pageSize)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testListMemberOfRoom() throws Exception {

        ResultActions actions = mockMvc.perform(get("/api/qc/wework/msg/room/"+roomId+"/members")
                .header("token", token)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testTransferVoiceAmr() throws Exception {

        ResultActions actions = mockMvc.perform(put("/api/qc/wework/msg/media/voice/mp3")
                        .header("token", token)
                        .param("msgId", "348413")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        System.out.println(actions.andReturn().getResponse().getContentAsString());
    }
}
