package miyamoto.comunicaco_api.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import miyamoto.comunicaco_api.api.dto.ComunicacaoInDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.business.service.ComunicacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/comunicacao")
@Tag(name="Mensageiro", description = "Envia mensagem para o usuário")

public class ComunicacaoController {

    private final ComunicacaoService service;

    public ComunicacaoController(ComunicacaoService service) {
        this.service = service;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar mensagem", description = "Cria uma nova mensagem")
    @ApiResponse(responseCode = "200", description = "Agendado mensagem com sucesso")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @GetMapping()
    @Operation(summary = "Buscar mensagem", description = "Buscar mensagem por email")
    @ApiResponse(responseCode = "200", description = "Mensagem encontrada")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Cancelar mensagem", description = "Cancela mensagem por email")
    @ApiResponse(responseCode = "200", description = "Mensagem cancelada")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }
}
