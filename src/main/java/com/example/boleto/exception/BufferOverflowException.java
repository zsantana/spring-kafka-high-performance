package com.example.boleto.exception;

/**
 * Exceção lançada quando o buffer interno atinge capacidade máxima
 */
public class BufferOverflowException extends RuntimeException {

    private final int tamanhoAtual;
    private final int capacidadeMaxima;

    public BufferOverflowException(int tamanhoAtual, int capacidadeMaxima) {
        super(String.format("Buffer overflow: tamanho atual %d excede capacidade máxima %d",
                tamanhoAtual, capacidadeMaxima));
        this.tamanhoAtual = tamanhoAtual;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}
