package br.com.fiap.email.repository;

import br.com.fiap.email.model.EmailEnviadoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailEnviadoRepository extends MongoRepository<EmailEnviadoModel, String> {
}
