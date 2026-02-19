package miyamoto.comunicaco_api.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@EqualsAndHashCode
public class ComunicacaoInDTO {

    @Schema(description = "Data e hora programada para o envio", example = "2026-12-25T10:00:00")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraEnvio;

    @Schema(description = "Nome completo do destinatário", example = "Fulano de Tal")
    private String nomeDestinatario;

    @Schema(description = "E-mail de destino", example = "cliente@email.com")
    private String emailDestinatario;

    @Schema(description = "Texto da mensagem a ser enviada", example = "Inserir aqui a sua mensagem")
    private String mensagem;

    @Schema(description = "Meio de comunicação utilizado", example = "EMAIL")
    private ModoEnvioEnum modoDeEnvio;

    @Schema(description = "Telefone do destinatário", example = "1212345678")
    private String telefoneDestinatario;

    @Schema(description = "Status do envio", example = "MENSAGEM")
    private StatusEnvioEnum statusEnvio;
}
