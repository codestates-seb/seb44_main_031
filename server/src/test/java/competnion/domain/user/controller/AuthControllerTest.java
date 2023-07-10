//package competnion.domain.user.controller;
//
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
//public class AuthControllerTest {
////    private final String USER_DEFULT_URI = "/auth";
////    @Autowired
////    private MockMvc mockMvc;
////    @Autowired
////    private Gson gson;
////    @MockBean
////    private AuthService service;
////    @Autowired
////    private UserRepository repository;
//////    @Autowired
//////    PasswordEncoder passwordEncoder;
////
////    @Test
////    void 이메일로_인증코드를_보냄() throws Exception {
////        // given
////        doNothing().when(service).sendVerificationEmail(anyString());
////
////        // when
////        mockMvc.perform(
////                get(USER_DEFULT_URI + "/send-verification-email")
////                        .contentType(MediaType.ALL)
////                        .accept(MediaType.ALL)
////                        .param("email", "dlawjdals0218@gmail.com")
////        )
////                .andExpect(status().isOk());
////    }
////
////    @Test
////    void 회원가입() throws Exception{
////        // given
////        SignUpRequest signUpRequest = new SignUpRequest(
////                "jeongmin0", "dlawjdals0218@gmail.com", "testpassword@1"
////        );
////        String jsonData = gson.toJson(signUpRequest);
////
////        doNothing().when(service).signUp(any(SignUpRequest.class));
////    }
//}