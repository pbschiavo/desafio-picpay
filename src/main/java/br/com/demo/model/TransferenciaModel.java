package br.com.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferenciaModel {

    private Integer recebedor;
    private Integer pagador;
    private BigDecimal valor;

}
