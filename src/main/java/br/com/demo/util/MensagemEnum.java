package br.com.demo.util;

public enum MensagemEnum {
    TRANSFERENCIA_SUCESSO("Você recebeu uma transferência no valor de R$ ");

    private final String mensagem;
    MensagemEnum(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
