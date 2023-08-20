package br.com.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class NotificacaoModel {

    private Integer id;
    private Integer recebedor;
    private BigDecimal valor;
    private String email;
    private String mensagem;
    private String status;
    private Date dataEnvio;
}
