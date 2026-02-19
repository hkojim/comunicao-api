package miyamoto.comunicaco_api.business.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import miyamoto.comunicaco_api.api.dto.ComunicacaoOutDTO;
import miyamoto.comunicaco_api.infraestructure.execptions.EmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;

    public void enviaEmail(ComunicacaoOutDTO dto){
        try{
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            mimeMessageHelper.setTo(InternetAddress.parse(dto.getEmailDestinatario()));
            mimeMessageHelper.setSubject("Comunicação da mensagem");


            Context context = new Context();
            context.setVariable("mensagem", dto.getMensagem());
            context.setVariable("dataHoraEnvio", dto.getDataHoraEnvio());

            String template = templateEngine.process("notificacao", context);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mensagem);

        } catch (Exception e) { // Captura MessagingException, RuntimeException, etc.
            throw new EmailException("Erro ao enviar o email ", e);
        }
    }
}
