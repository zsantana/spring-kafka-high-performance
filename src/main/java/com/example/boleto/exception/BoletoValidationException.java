package com.example.boleto.exception;

/**
 * Exceção lançada quando os dados de entrada do boleto são inválidos
 */
public class BoletoValidationException extends RuntimeException {

    private final String campo;
    private final String valorInvalido;

    public BoletoValidationException(String campo, String valorInvalido, String mensagem) {
        super(mensagem);
        this.campo = campo;
        this.valorInvalido = valorInvalido;
    }

    public BoletoValidationException(String mensagem) {
        super(mensagem);
        this.campo = null;
        this.valorInvalido = null;
    }

    public String getCampo() {
        return campo;
    }

    public String getValorInvalido() {
        return valorInvalido;
    }
}
