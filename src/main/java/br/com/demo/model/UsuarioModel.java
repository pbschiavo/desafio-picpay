package br.com.demo.model;

import br.com.demo.util.TipoUsuarioEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UsuarioModel {

    private Integer id;

    @NotNull(message = "O nome não pode estar vazio")
    @Size(min = 1, max = 30, message = "O nome deve ter entre {min} e {max} caracteres")
    private String nome;

    @NotNull(message = "O CPF/CNPJ não pode estar vazio")
    @Size(min = 11, max = 14, message = "O CPF/CNPJ deve ter entre {min} e {max} caracteres")
    private String cpfCnpj;

    @NotNull(message = "O email não pode estar vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotNull(message = "A senha não pode estar vazia")
    @Size(min = 6, max = 10, message = "A senha deve ter pelo menos {min} caracteres")
    private String senha;

    @NotNull(message = "O saldo não pode ser negativo")
    private BigDecimal saldo;

    @NotNull(message = "O tipo deve ser preenchido")
    private TipoUsuarioEnum tipo;
}

