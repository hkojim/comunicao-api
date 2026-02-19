package miyamoto.comunicaco_api.business.service;

import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTOFixture;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTOFixture;
import miyamoto.comunicaco_api.business.converter.ComunicacaoConverter;
import miyamoto.comunicaco_api.business.mapper.ComunicacaoConverterMapper;
import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.execptions.IllegalArgumentException;
import miyamoto.comunicaco_api.infraestructure.execptions.IllegalStateException;
import miyamoto.comunicaco_api.infraestructure.execptions.ResourceNotFoundException;
import miyamoto.comunicaco_api.infraestructure.repositories.ComunicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunicacaoServiceTest {

    @InjectMocks
    private ComunicacaoService comunicacaoService;

    @Mock
    private ComunicacaoRepository repository;
    @Mock
    private ComunicacaoConverter converter;
    @Mock
    private ComunicacaoConverterMapper mapper;

    ComunicacaoEntity comunicacaoEntity;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    LocalDateTime dataHora;
    String email;

    @BeforeEach
    void setUp() {
        dataHora = LocalDateTime.of(2026, 2, 20, 10, 30, 0);
        email = "musashi@email.com";

        comunicacaoEntity = ComunicacaoEntity.builder()
                .id(123L)
                .dataHoraEnvio(dataHora)
                .nomeDestinatario("Miyamoto Musashi")
                .emailDestinatario(email)
                .telefoneDestinatario("1211112222")
                .mensagem("A estratégia é a arte do guerreiro.")
                .modoDeEnvio(ModoEnvioEnum.EMAIL)
                .statusEnvio(StatusEnvioEnum.PENDENTE)
                .build();

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(
                dataHora,
                "Miyamoto Musashi",
                email,
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                "1211112222",
                StatusEnvioEnum.PENDENTE
        );

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
    void deveAgendarComunicacaoComSucesso() {
        when(mapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(repository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(mapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO dto = comunicacaoService.agendarComunicacao(comunicacaoInDTO);

        assertEquals(dto, comunicacaoOutDTO);
        assertEquals(StatusEnvioEnum.PENDENTE, comunicacaoInDTO.getStatusEnvio());
        verify(mapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(repository).save(comunicacaoEntity);
        verify(mapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void naoDeveAgendarComunicacaoCasoDTONull() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            comunicacaoService.agendarComunicacao(null);
        });

        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Dados de comunicação não podem ser nulos."));
        verifyNoInteractions(repository, mapper);
    }

    @Test
    void deveBuscarStatusComunicacaoComSucesso() {
        when(repository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(mapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO dto = comunicacaoService.buscarStatusComunicacao(email);

        assertEquals(dto, comunicacaoOutDTO);
        verify(repository).findByEmailDestinatario(email);
        verify(mapper).paraComunicacaoOutDTO(comunicacaoEntity);
    }

    @Test
    void deveGerarExcecaoAoBuscarStatusCasoEmailEmBranco() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            comunicacaoService.buscarStatusComunicacao("");
        });

        assertThat(e.getMessage(), is("O e-mail do destinatário deve ser informado."));
        verifyNoInteractions(repository, mapper);
    }

    @Test
    void deveGerarExcecaoAoBuscarStatusCasoComunicacaoNaoEncontrada() {
        when(repository.findByEmailDestinatario(email)).thenReturn(null);

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            comunicacaoService.buscarStatusComunicacao(email);
        });

        assertThat(e.getMessage(), is("Comunicação não encontrada para o e-mail: " + email));
        verify(repository).findByEmailDestinatario(email);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveAlterarStatusParaCanceladoComSucesso() {
        when(repository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(repository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(mapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);

        ComunicacaoOutDTO dto = comunicacaoService.alterarStatusComunicacao(email);

        assertEquals(StatusEnvioEnum.CANCELADO, comunicacaoEntity.getStatusEnvio());
        verify(repository).findByEmailDestinatario(email);
        verify(repository).save(comunicacaoEntity);
    }

    @Test
    void deveGerarExcecaoAoAlterarStatusCasoComunicacaoJaEnviada() {
        comunicacaoEntity.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        when(repository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            comunicacaoService.alterarStatusComunicacao(email);
        });

        assertThat(e.getMessage(), is("Não é possível cancelar uma comunicação que já foi enviada."));
        verify(repository).findByEmailDestinatario(email);
        verify(repository, never()).save(any());
    }
}