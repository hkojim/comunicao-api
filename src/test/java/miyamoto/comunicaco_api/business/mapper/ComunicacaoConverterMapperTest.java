package miyamoto.comunicaco_api.business.mapper;

import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTOFixture;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTOFixture;
import miyamoto.comunicaco_api.infraestructure.entities.ComunicacaoEntity;
import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ComunicacaoConverterMapperTest {


    ComunicacaoConverterMapper mapper;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoEntity comunicacaoEntity;
    LocalDateTime dataHora;

    @BeforeEach
    void setUp() {
        // Inicializa o mapper gerado pelo MapStruct
        mapper = Mappers.getMapper(ComunicacaoConverterMapper.class);
        dataHora = LocalDateTime.of(2026, 2, 20, 10, 30, 0);

        comunicacaoEntity = ComunicacaoEntity.builder()
                .id(123L)
                .dataHoraEnvio(dataHora)
                .nomeDestinatario("Miyamoto Musashi")
                .emailDestinatario("musashi@email.com")
                .telefoneDestinatario("1211112222")
                .mensagem("A estratégia é a arte do guerreiro.")
                .modoDeEnvio(ModoEnvioEnum.valueOf("EMAIL"))
                .statusEnvio(StatusEnvioEnum.valueOf("PENDENTE"))
                .build();

        // Mock do DTO de entrada usando sua Fixture
        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                12L,
                dataHora,
                "Miyamoto Musashi",
                "musashi@email.com",
                "1211112222",
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                StatusEnvioEnum.PENDENTE
        );

        // Mock do DTO de entrada usando sua Fixture
        comunicacaoInDTO = ComunicacaoInDTOFixture.build(
                dataHora,
                "Miyamoto Musashi",
                "musashi@email.com",
                "A estratégia é a arte do guerreiro.",
                ModoEnvioEnum.EMAIL,
                "1211112222",
                StatusEnvioEnum.PENDENTE
        );
    }

    @Test
    void deveConverterParaComunicacaoEntity() {
        ComunicacaoEntity entityResultado = mapper.paraComunicacaoEntity(comunicacaoInDTO);
        assertAll("Verificando conversão para Entity",
                () -> assertNotNull(entityResultado),
                () -> assertEquals(comunicacaoInDTO.getNomeDestinatario(), entityResultado.getNomeDestinatario()),
                () -> assertEquals(comunicacaoInDTO.getMensagem(), entityResultado.getMensagem())
        );
        assertNotNull(entityResultado);
    }

    @Test
    void deveConverterParaComunicacaoOutDTO() {
        ComunicacaoOutDTO dtoResultado = mapper.paraComunicacaoOutDTO(comunicacaoEntity);
        assertNotNull(dtoResultado, "O DTO de saída não deve ser nulo");
        assertEquals(comunicacaoOutDTO.getNomeDestinatario(), dtoResultado.getNomeDestinatario());
        assertEquals(comunicacaoOutDTO.getMensagem(), dtoResultado.getMensagem());
    }
}