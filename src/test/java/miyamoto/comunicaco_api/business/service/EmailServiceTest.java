package miyamoto.comunicaco_api.business.service;

import jakarta.mail.internet.MimeMessage;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTOFixture;
import miyamoto.comunicaco_api.infraestructure.execptions.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    // Usamos um mock real para o MimeMessage
    @Mock
    private MimeMessage mimeMessage;

    ComunicacaoOutDTO comunicacaoOutDTO;
    LocalDateTime dataHora;
    String remetente = "sistema@teste.com";
    String nomeRemetente = "Remetente Teste";

    @BeforeEach
    void setUp() {
        dataHora = LocalDateTime.of(2026, 2, 20, 10, 30, 0);

        // Injeção manual dos valores @Value via Reflection (Obrigatório para teste unitário)
        ReflectionTestUtils.setField(emailService, "remetente", remetente);
        ReflectionTestUtils.setField(emailService, "nomeRemetente", nomeRemetente);

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(
                1L,
                dataHora,
                "Miyamoto Musashi",
                "musashi@email.com",
                "1199999999",
                "Mensagem de teste",
                null,
                null
        );
    }

    @Test
    void deveEnviarEmailComSucesso() {
        // GIVEN
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("notificacao"), any(Context.class))).thenReturn("html-renderizado");

        // WHEN
        emailService.enviaEmail(comunicacaoOutDTO);

        // THEN
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
        verify(templateEngine).process(eq("notificacao"), any(Context.class));
    }

    @Test
    void deveGerarExcecaoCasoOcorraErroNoEnvio() {
        // GIVEN
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        // Simulando erro no processamento do template para disparar o catch
        when(templateEngine.process(eq("notificacao"), any(Context.class)))
                .thenThrow(new RuntimeException("Erro ao processar template"));

        // WHEN
        EmailException e = assertThrows(EmailException.class, () -> {
            emailService.enviaEmail(comunicacaoOutDTO);
        });

        // THEN
        assertThat(e, notNullValue());
        // Verifique se a String no seu código tem o espaço no final: "Erro ao enviar o email "
        assertThat(e.getMessage(), is("Erro ao enviar o email "));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void deveProcessarTemplateComDadosCorretos() {
        // GIVEN
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("notificacao"), any(Context.class))).thenReturn("template");

        // WHEN
        emailService.enviaEmail(comunicacaoOutDTO);

        // THEN
        // Verifica se o templateEngine foi chamado exatamente com o nome do template esperado
        verify(templateEngine, times(1)).process(eq("notificacao"), any(Context.class));
    }
}