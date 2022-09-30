package br.com.fiap.email.controller;

import br.com.fiap.email.model.EmailEnviadoModel;
import br.com.fiap.email.repository.EmailEnviadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/enviado")
@AllArgsConstructor
public class EmailEnviadoController {

    @Autowired
    private EmailEnviadoRepository emailEnviadoRepository;


    @GetMapping
    private ResponseEntity<List<EmailEnviadoModel>> findAllEnviados(){
        return ResponseEntity.ok(emailEnviadoRepository.findAll());
    }
}
