package br.com.fiap.email.controller;

import br.com.fiap.email.model.EmailTemplate;
import br.com.fiap.email.repository.TemplateEmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/template")
@AllArgsConstructor
public class TemplateEmailController {

    @Autowired
    private TemplateEmailRepository templateRepository;

    @GetMapping("/{chave}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<EmailTemplate> obterPorchave(@PathVariable String chave) {
        Optional<EmailTemplate> template = templateRepository.obterPorChave(chave);
        return new ResponseEntity<EmailTemplate>(template.get(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<EmailTemplate>> findAll(){
        return new ResponseEntity<>(templateRepository.findAll(), HttpStatus.OK);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EmailTemplate> salvar(@RequestBody EmailTemplate template) {
        Optional<EmailTemplate> templateExistente = templateRepository.obterPorChave(template.getChave());

        if (templateExistente.isPresent()) {
            return new ResponseEntity("Template já criado", HttpStatus.BAD_REQUEST);
        }

        templateRepository.save(template);
        return new ResponseEntity<EmailTemplate>(template, HttpStatus.CREATED);
    }
}

