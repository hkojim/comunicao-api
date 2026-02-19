package miyamoto.comunicaco_api.api.dto;

import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;

import java.time.LocalDateTime;

public class ComunicacaoInDTOFixture {
    public static ComunicacaoInDTO build(LocalDateTime dataHoraEnvio,
                                         String nomeDestinatario,
                                         String emailDestinatario,
                                         String mensagem,
                                         ModoEnvioEnum modoDeEnvio,
                                         String telefoneDestinatario,
                                         StatusEnvioEnum statusEnvio) {
        return new ComunicacaoInDTO(dataHoraEnvio,
                nomeDestinatario,
                emailDestinatario,
                mensagem,
                modoDeEnvio,
                telefoneDestinatario,
                statusEnvio);
    }

}
