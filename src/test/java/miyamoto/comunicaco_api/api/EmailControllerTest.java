package miyamoto.comunicaco_api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTOFixture;
import miyamoto.comunicaco_api.business.service.EmailService;
import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ComunicacaoOutDTO comunicacaoOutDTO;

    @BeforeEach
    void setUp() {
        // Registra o módulo de data para evitar erros de serialização do LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(emailController)
                .alwaysDo(print())
                .build();

        // Criando o objeto de saída usando o Fixture que você já possui
        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                1L,
                LocalDateTime.now(),
                "Miyamoto Musashi",
                "musashi@email.com",
                "1211112222",
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );
    }

    @Test
    void deveEnviarEmailComSucesso() throws Exception {
        // Converte o objeto para JSON
        String json = objectMapper.writeValueAsString(comunicacaoOutDTO);

        // Executa a requisição POST para /email
        mockMvc.perform(post("/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()); // Valida o retorno 200 OK

        // Verifica se o service foi chamado exatamente uma vez com qualquer ComunicacaoOutDTO
        verify(emailService).enviaEmail(any(ComunicacaoOutDTO.class));
    }
}