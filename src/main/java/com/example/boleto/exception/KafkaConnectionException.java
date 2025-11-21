package com.example.boleto.exception;

/**
 * Exceção lançada quando há problemas de conexão com o Kafka
 */
public class KafkaConnectionException extends RuntimeException {

    private final String topico;
    private final boolean isRecoverable;

    public KafkaConnectionException(String topico, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.topico = topico;
        this.isRecoverable = true;
    }

    public KafkaConnectionException(String topico, String mensagem, Throwable causa, boolean isRecoverable) {
        super(mensagem, causa);
        this.topico = topico;
        this.isRecoverable = isRecoverable;
    }

    public String getTopico() {
        return topico;
    }

    public boolean isRecoverable() {
        return isRecoverable;
    }
}
