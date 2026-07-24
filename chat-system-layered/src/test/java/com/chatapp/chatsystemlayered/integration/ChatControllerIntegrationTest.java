package com.chatapp.chatsystemlayered.integration;

import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testJoinSendAndRetrieveHistory() throws Exception {

        // Join room
        mockMvc.perform(post("/api/chat/join/room1")
                        .header("X-User-Id", "ann"))
                .andExpect(status().isOk());

        // Send message
        ChatMessageDTO dto = new ChatMessageDTO("room1", "ann", "Hello world", System.currentTimeMillis());

        mockMvc.perform(post("/api/chat/send/room1")
                        .header("X-User-Id", "ann")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Retrieve history
        mockMvc.perform(get("/api/chat/history/room1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello world"))
                .andExpect(jsonPath("$[0].senderId").value("ann"));
    }
}
