package com.metacoding.restserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc // MockMvc IoC에 등록해줌
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper om = new ObjectMapper();

    @Test
    public void login_test() throws Exception {
        // given
        UserRequest.LoginDTO dto = new UserRequest.LoginDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/login").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        String jwt = actions.andReturn().getResponse().getHeader("Authorization");
        System.out.println("mma: " + jwt);

        // then
        actions.andExpect(header().string("Authorization", Matchers.startsWith("Bearer")));
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(1));
        actions.andExpect(jsonPath("$.data.username").value("ssar"));
    }

    @Test
    public void join_test() throws Exception {
        // given
        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("haha");
        dto.setPassword("1234");
        dto.setEmail("haha@nate.com");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        // then
        // 배열찾기 팁 : $.data[0].id
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(4));
        actions.andExpect(jsonPath("$.data.username").value("haha"));
        actions.andExpect(jsonPath("$.data.email").value("haha@nate.com"));
        actions.andExpect(jsonPath("$.data.createdAt").isNotEmpty());
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");
        dto.setEmail("ssar@nate.com");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        // then
        // 배열찾기 팁 : $.data[0].id
        actions.andExpect(jsonPath("$.success").value(false));
        actions.andExpect(jsonPath("$.message").value("유저네임 중복"));
        actions.andExpect(jsonPath("$.data").isEmpty());
    }
}
