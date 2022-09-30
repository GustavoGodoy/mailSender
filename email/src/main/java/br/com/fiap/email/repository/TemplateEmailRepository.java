package br.com.fiap.email.repository;

import br.com.fiap.email.model.EmailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TemplateEmailRepository extends MongoRepository<EmailTemplate, String> {

    @Query("{chave: '?0'}")
    Optional<EmailTemplate> obterPorChave(String chave);

}
