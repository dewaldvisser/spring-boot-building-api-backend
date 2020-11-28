package com.example.copsboot.user.web;

import com.example.copsboot.infrastructure.CopsbootControllerTest;
import com.example.copsboot.infrastructure.SpringProfiles;
import com.example.copsboot.security.OAuth2ServerConfiguration;
import com.example.copsboot.security.SecurityConfiguration;
import com.example.copsboot.security.StubUserDetailsService;
import com.example.copsboot.user.UserService;
import com.example.copsboot.user.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.example.copsboot.security.SecurityHelperForMockMvc.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//tag::webmvctest[]
@RunWith(SpringRunner.class) //<1>
@CopsbootControllerTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc; //<4>

    //tag::extra-fields[]
    @Autowired
    private ObjectMapper objectMapper; //<1>
    @MockBean
    private UserService service; //<2>
    //end::extra-fields[]

    //tag::notauth[]
    @Test
    public void givenNotAuthenticated_whenAskingMyDetails_forbidden() throws Exception {
        mvc.perform(get("/api/users/me")) //<1>
           .andExpect(status().isUnauthorized()); //<2>
    }
    //end::notauth[]

    //tag::authofficer[]
    @Test
    public void givenAuthenticatedAsOfficer_whenAskingMyDetails_detailsReturned() throws Exception {
        String accessToken = obtainAccessToken(mvc, Users.OFFICER_EMAIL, Users.OFFICER_PASSWORD); //<1>

        when(service.getUser(Users.officer().getId())).thenReturn(Optional.of(Users.officer())); //<2>

        mvc.perform(get("/api/users/me") //<3>
                            .header(HEADER_AUTHORIZATION, bearer(accessToken))) //<4>
           .andExpect(status().isOk()) //<5>
           .andExpect(jsonPath("id").exists()) //<6>
           .andExpect(jsonPath("email").value(Users.OFFICER_EMAIL))
           .andExpect(jsonPath("roles").isArray())
           .andExpect(jsonPath("roles[0]").value("OFFICER"))
        ;
    }
    //end::authofficer[]

    //tag::authcaptain[]
    @Test
    public void givenAuthenticatedAsCaptain_whenAskingMyDetails_detailsReturned() throws Exception {
        String accessToken = obtainAccessToken(mvc, Users.CAPTAIN_EMAIL, Users.CAPTAIN_PASSWORD);

        when(service.getUser(Users.captain().getId())).thenReturn(Optional.of(Users.captain()));

        mvc.perform(get("/api/users/me")
                            .header(HEADER_AUTHORIZATION, bearer(accessToken)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("id").exists())
           .andExpect(jsonPath("email").value(Users.CAPTAIN_EMAIL))
           .andExpect(jsonPath("roles").isArray())
           .andExpect(jsonPath("roles").value("CAPTAIN"));
    }
    //end::authcaptain[]

    //tag::test-create-officer[]
    @Test
    public void testCreateOfficer() throws Exception {
        String email = "wim.deblauwe@example.com";
        String password = "my-super-secret-pwd";

        CreateOfficerParameters parameters = new CreateOfficerParameters();
        parameters.setEmail(email);
        parameters.setPassword(password);

        when(service.createOfficer(email, password)).thenReturn(Users.newOfficer(email, password)); //<1>

        mvc.perform(post("/api/users") //<2>
                .contentType(MediaType.APPLICATION_JSON_UTF8) //<3>
                .content(objectMapper.writeValueAsString(parameters))) //<4>
                .andExpect(status().isCreated()) //<5>
                .andExpect(jsonPath("id").exists()) //<6>
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("roles").isArray())
                .andExpect(jsonPath("roles[0]").value("OFFICER"));

        verify(service).createOfficer(email, password); //<7>
    }

    @Test
    public void testCreateOfficerIfPasswordIsTooShort() throws Exception {
        String email = "wim.deblauwe@example.com";
        String password = "pwd";

        CreateOfficerParameters parameters = new CreateOfficerParameters();
        parameters.setEmail(email);
        parameters.setPassword(password);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(parameters)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].fieldName").value("password"));

        verify(service, never()).createOfficer(email, password);
    }

   @Test
   public void testCreateOfficerIfEmailIsInvalid() throws Exception {
        String email = "wim.deblauwe";
        String password = "my-super-secret-pwd";

        CreateOfficerParameters parameters = new CreateOfficerParameters();
        parameters.setEmail(email);
        parameters.setPassword(password);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(parameters)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].fieldName").value("email"));

        verify(service, never()).createOfficer(email, password);
   }
}
