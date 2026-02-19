package miyamoto.comunicaco_api.api.dto;

import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;

import java.time.LocalDateTime;

public class ComunicacaoOutDTOFixture {
    public static ComunicacaoOutDTO build(Long id,
                                         LocalDateTime dataHoraEnvio,
                                         String nomeDestinatario,
                                         String emailDestinatario, String telefoneDestinatario,
                                         String mensagem,
                                         ModoEnvioEnum modoDeEnvio,
                                         StatusEnvioEnum statusEnvio) {
        return new ComunicacaoOutDTO(id,
                dataHoraEnvio,
                nomeDestinatario,
                emailDestinatario,
                telefoneDestinatario,
                mensagem,
                modoDeEnvio,
                statusEnvio);
    }
}
