package miyamoto.comunicaco_api.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTOFixture;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTOFixture;
import miyamoto.comunicaco_api.business.service.ComunicacaoService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ComunicacaoControllerTest {

    @InjectMocks
    private ComunicacaoController comunicacaoController;

    @Mock
    private ComunicacaoService comunicacaoService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String url;
    private String json;
    private ComunicacaoInDTO comunicacaoInDTO;
    private ComunicacaoOutDTO comunicacaoOutDTO;

    LocalDateTime dataHora;
    String email;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(comunicacaoController).alwaysDo(print()).build();

        url = "/comunicacao";
        dataHora = LocalDateTime.of(2026, 2, 20, 10, 30, 0);
        email = "musashi@email.com";

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(
                dataHora,
                "Miyamoto Musashi",
                email,
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                "1211112222",
                StatusEnvioEnum.PENDENTE
        );
        json = objectMapper.writeValueAsString(comunicacaoInDTO);

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                123L,
                dataHora,
                "Miyamoto Musashi",
                email,
                "1211112222",
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );
    }

    @Test
    void deveAgendarComunicacaoComSucesso()throws Exception {
        when(comunicacaoService.agendarComunicacao(any())).thenReturn(comunicacaoOutDTO);

        mockMvc.perform(post("/comunicacao/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated()); // Ou .isOk() se usar 200

        verify(comunicacaoService).agendarComunicacao(any(ComunicacaoInDTO.class));
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void deveBuscarStatusComSucesso() throws Exception {
        // 1. Cenário: Definir o e-mail de busca e o retorno esperado
        String emailBusca = email;
        when(comunicacaoService.buscarStatusComunicacao(emailBusca)).thenReturn(comunicacaoOutDTO);

        // 2. Execução e Validação
        mockMvc.perform(get(url) // Rota base definida no @RequestMapping da classe
                        .param("emailDestinatario", emailBusca) // Passa o @RequestParam
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Valida o status 200 conforme o @ApiResponse

        // 3. Verificações de segurança
        verify(comunicacaoService).buscarStatusComunicacao(emailBusca);
        verifyNoMoreInteractions(comunicacaoService);
    }

    @Test
    void deveCancelarStatusComSucesso() throws Exception {
        // 1. Cenário: Definir o e-mail para cancelamento e o retorno esperado
        String emailParaCancelar = email;

        // Configuramos o mock para retornar o DTO com o status atualizado (CANCELADO)
        comunicacaoOutDTO.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        when(comunicacaoService.alterarStatusComunicacao(emailParaCancelar)).thenReturn(comunicacaoOutDTO);

        // 2. Execução e Validação
        mockMvc.perform(patch("/comunicacao/cancelar") // Rota completa: @RequestMapping + @PatchMapping
                        .param("emailDestinatario", emailParaCancelar) // Passa o @RequestParam
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Valida o status 200 conforme o @ApiResponse

        // 3. Verificações
        verify(comunicacaoService).alterarStatusComunicacao(emailParaCancelar);
        verifyNoMoreInteractions(comunicacaoService);
    }
}
