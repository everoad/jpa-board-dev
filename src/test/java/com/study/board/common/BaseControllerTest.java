package com.study.board.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.board.config.AuthServerConfigTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@Disabled
public class BaseControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public AppProperties appProperties;

    @Autowired
    public ObjectMapper objectMapper;

    public String accessToken;

    @BeforeEach
    public void setUp() throws Exception {
        AuthServerConfigTest authServerConfigTest = new AuthServerConfigTest();
        if (StringUtils.isEmpty(this.accessToken)) {
            this.accessToken = authServerConfigTest.getAccessToken(mockMvc, appProperties);
        }
    }

    public <T> ResultActions postRequest(String url, T content) throws Exception {
        MockHttpServletRequestBuilder builder = post(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON);

        if (content != null) {
            builder.content(objectMapper.writeValueAsString(content));
        }

        return mockMvc.perform(builder);
    }

    public <T> ResultActions deleteRequest(String url, T content) throws Exception {
        MockHttpServletRequestBuilder builder = delete(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON);

        if (content != null) {
            builder.content(objectMapper.writeValueAsString(content));
        }

        return mockMvc.perform(builder);
    }

    public <T> ResultActions putRequest(String url, T content) throws Exception {
        MockHttpServletRequestBuilder builder = put(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON);

        if (content != null) {
            builder.content(objectMapper.writeValueAsString(content));
        }

        return mockMvc.perform(builder);
    }

    public ResultActions getRequest(String url, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder builder = get(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken);

        if (params != null) {
            MultiValueMap<String, String> ps = new LinkedMultiValueMap<>();
            ps.setAll(params);
            builder.params(ps);
        }

        return mockMvc.perform(builder);
    }


}
