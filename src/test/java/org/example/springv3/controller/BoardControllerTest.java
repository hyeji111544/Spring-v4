package org.example.springv3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.board.BoardRequest;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional // 컨트롤러에서 메서드가 실행되고 종료될 때 롤밷됨
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();
    private String accessToken;

    @BeforeEach
    public void setUp() {
        System.out.println("나 실행돼? setUp");
        User sessionUser = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(sessionUser);
    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("title11");
        reqDTO.setContent("content11");

        String requestBody = om.writeValueAsString(reqDTO); // json 문자열로 바꾸는 역할
        //System.out.println(requestBody);
        // when
        ResultActions actions = mvc.perform(
                post("/api/board")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)

        );
        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body.id").value(11));
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 2;

        // when
        ResultActions actions = mvc.perform(
                delete("/api/board/"+id)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(status().isOk());
        //actions.andExpect(jsonPath("$.msg").value("성공"));
        //actions.andExpect(jsonPath("$.body").isEmpty());


    }
}
