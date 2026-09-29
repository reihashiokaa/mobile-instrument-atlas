package com.example.drawerlayout;

public class EstadoOperacao {

    public enum Status {
        OCIOSO,
        PROCESSANDO,
        SUCESSO,
        ERRO
    }

    public final Status status;
    public final String codigo;
    public final String mensagem;

    public EstadoOperacao(Status status, String codigo, String mensagem) {
        this.status = status;
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public static EstadoOperacao ocioso() {
        return new EstadoOperacao(Status.OCIOSO, null, null);
    }

    public static EstadoOperacao processando() {
        return new EstadoOperacao(Status.PROCESSANDO, null, null);
    }

    public static EstadoOperacao sucesso() {
        return new EstadoOperacao(Status.SUCESSO, "OK", null);
    }

    public static EstadoOperacao erro(String codigo, String mensagem) {
        return new EstadoOperacao(Status.ERRO, codigo, mensagem);
    }
}