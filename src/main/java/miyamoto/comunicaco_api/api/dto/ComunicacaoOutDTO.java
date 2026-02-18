package miyamoto.comunicaco_api.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import miyamoto.comunicaco_api.infraestructure.enums.ModoEnvioEnum;
import miyamoto.comunicaco_api.infraestructure.enums.StatusEnvioEnum;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ComunicacaoOutDTO{


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraEnvio;
    private String nomeDestinatario;
    private String emailDestinatario;
    private String telefoneDestinatario;
    private String mensagem;
    private ModoEnvioEnum modoDeEnvio;
    private StatusEnvioEnum statusEnvio;

}
