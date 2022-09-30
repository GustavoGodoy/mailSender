package br.com.fiap.email.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class EmailModel {
    protected String chave;
    protected List<String> destinatarios;
    protected List<String> destinatariosCopia;
    protected HashMap<String, String> dados;
}
