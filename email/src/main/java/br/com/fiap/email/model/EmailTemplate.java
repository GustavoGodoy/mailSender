package br.com.fiap.email.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("templates")
@Data
public class EmailTemplate {
    @Id
    private String id;
    private String chave;
    private String assunto;
    private String corpoEmail;
    private String remetente;

    public void replaceMail(String chave, String valor) {
        this.assunto = this.assunto.replace(chave, valor);
        this.corpoEmail = this.corpoEmail.replace("<"+chave+">", valor);
    }
}
