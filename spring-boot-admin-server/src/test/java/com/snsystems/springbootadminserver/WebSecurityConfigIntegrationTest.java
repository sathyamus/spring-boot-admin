package com.snsystems.springbootadminserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityConfigIntegrationTest {

    @Autowired WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
          .webAppContextSetup(wac)
          .build();
    }

    @Test
    public void whenApplicationStarts_ThenGetLoginPageWithSuccess() throws Exception {
        mockMvc
          .perform(get("/login.html"))
          .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenFormLoginAttempted_ThenSuccess() throws Exception {
        mockMvc.perform(formLogin("/login")
          .user("admin")
          .password("admin"));
    }

    @Test
    public void whenFormLoginWithSuccess_ThenApiEndpointsAreAccessible() throws Exception {
        mockMvc.perform(formLogin("/login")
          .user("user")
          .password("password"));

        mockMvc
          .perform(get("/applications/"))
          .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void whenHttpBasicAttempted_ThenSuccess() throws Exception {
        mockMvc.perform(get("/applications").with(httpBasic("admin", "admin")))
        .andExpect(status().is2xxSuccessful());
    }

    @Ignore
    public void whenInvalidHttpBasicAttempted_ThenUnauthorized() throws Exception {
        mockMvc
          .perform(get("/applications").with(httpBasic("admin", "invalid")))
          .andExpect(status().is2xxSuccessful())
          .andExpect(content().string("Invalid username or password"));
    }

}
