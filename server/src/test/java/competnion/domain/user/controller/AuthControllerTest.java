package competnion.domain.user.controller;

import com.google.gson.Gson;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.repository.UserRepository;
import competnion.domain.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    private final String USER_DEFULT_URI = "/auth";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private AuthService service;
    @Autowired
    private UserRepository repository;
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Test
    void 이메일로_인증코드를_보냄() throws Exception {
        // given
        doNothing().when(service).sendVerificationEmail(anyString());

        // when
        mockMvc.perform(
                get(USER_DEFULT_URI + "/send-verification-email")
                        .contentType(MediaType.ALL)
                        .accept(MediaType.ALL)
                        .param("email", "dlawjdals0218@gmail.com")
        )
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입() throws Exception{
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "jeongmin0", "dlawjdals0218@gmail.com", "testpassword@1"
        );
        String jsonData = gson.toJson(signUpRequest);

        doNothing().when(service).signUp(any(SignUpRequest.class));
    }
}