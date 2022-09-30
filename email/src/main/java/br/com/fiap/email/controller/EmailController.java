package br.com.fiap.email.controller;

import br.com.fiap.email.model.EmailEnviadoModel;
import br.com.fiap.email.model.EmailModel;
import br.com.fiap.email.model.EmailTemplate;
import br.com.fiap.email.repository.EmailEnviadoRepository;
import br.com.fiap.email.repository.TemplateEmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/email")
@AllArgsConstructor
public class EmailController {

    private JavaMailSender emailSender;
    @Autowired
    private TemplateEmailRepository templateRepository;
    @Autowired
    private EmailEnviadoRepository emailEnviadoRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<EmailEnviadoModel> enviar(@RequestBody EmailModel model) {

        if (model.getDados() == null) {
            return new ResponseEntity("Informe o Dicionario de Dados", HttpStatus.BAD_REQUEST);
        }
        Optional<EmailTemplate> presentTemplate = templateRepository.obterPorChave(model.getChave());

        if (!presentTemplate.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        for (String key : model.getDados().keySet()) {
            presentTemplate.get().replaceMail(key, model.getDados().get(key));
        }

        EmailModel mailModel = new EmailEnviadoModel();
        EmailEnviadoModel sentMail = (EmailEnviadoModel) mailModel;
        sentMail.setAssuntoEmail(presentTemplate.get().getAssunto());
        sentMail.setCorpoEmail(presentTemplate.get().getCorpoEmail());
        sentMail.setDestinatarios(model.getDestinatarios());
        sentMail.setDestinatariosCopia(model.getDestinatariosCopia());
        sentMail.setDataEnvio(java.time.LocalDateTime.now().toString());

        SimpleMailMessage emailMessage = new SimpleMailMessage();

        String[] destinatarios = new String[sentMail.getDestinatarios().size()];
        destinatarios = sentMail.getDestinatarios().toArray(destinatarios);

        String[] destinatariosCopia = new String[sentMail.getDestinatariosCopia().size()];
        destinatariosCopia = sentMail.getDestinatariosCopia().toArray(destinatariosCopia);

        try {
            emailMessage.setFrom(presentTemplate.get().getRemetente());
            emailMessage.setTo(destinatarios);
            emailMessage.setCc(destinatariosCopia);
            emailMessage.setSubject(sentMail.getAssuntoEmail());
            emailMessage.setText(sentMail.getCorpoEmail());
            emailSender.send(emailMessage);
        } catch (MailException ex) {
            sentMail.setEnviadoComSucesso(false);
            emailEnviadoRepository.save(sentMail);
            return new ResponseEntity<EmailEnviadoModel>(sentMail, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        emailEnviadoRepository.save(sentMail);
        return new ResponseEntity(HttpStatus.OK);
    }
}
