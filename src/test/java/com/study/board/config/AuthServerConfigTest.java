package com.study.board.config;

import com.study.board.common.AppProperties;
import com.study.board.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthServerConfigTest extends BaseControllerTest {


    @Test
    public void 엑세스_토큰_발급() throws Exception {
        getAccessToken(mockMvc, appProperties);
    }

    public String getAccessToken(MockMvc mockMvc, AppProperties appProperties) throws Exception {
        // Given
        String testUserName = appProperties.getTestUserName();
        String testUserPassword = appProperties.getTestUserPassword();
        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();
        String grantType = appProperties.getGrantType();

        // When & Then
        String content = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", testUserName)
                .param("password", testUserPassword)
                .param("grant_type", grantType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists())
                .andReturn().getResponse().getContentAsString();

        Jackson2JsonParser parser = new Jackson2JsonParser();
        String access_token = parser.parseMap(content).get("access_token").toString();

        return "Bearer " + access_token;
    }
}