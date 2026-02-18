package miyamoto.comunicaco_api.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.business.service.ComunicacaoService;
import miyamoto.comunicaco_api.infraestructure.execptions.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comunicacao")
@Tag(name = "Mensageiro", description = "Recursos para agendamento e monitoramento de comunicações")
public class ComunicacaoController {

    private final ComunicacaoService service;

    public ComunicacaoController(ComunicacaoService service) {
        this.service = service;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar nova mensagem", description = "Registra uma solicitação de envio na base de dados.")
    @ApiResponse(responseCode = "201", description = "Mensagem agendada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflito: Agendamento duplicado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody @Valid ComunicacaoInDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.agendarComunicacao(dto));
    }

    @GetMapping
    @Operation(summary = "Consultar status por e-mail", description = "Retorna os detalhes da última comunicação para o destinatário informado.")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Destinatário sem agendamentos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(
            @Parameter(description = "E-mail do destinatário", example = "usuario@provedor.com")
            @RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Cancelar agendamento", description = "Altera o status de uma mensagem pendente para CANCELADO.")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }
}