package com.example.boleto.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.boleto.dto.ErrorResponse;

/**
 * Manipulador global de exceções para toda a aplicação
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata erros de validação de entrada (Bean Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.warn("Erro de validação: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Dados de entrada inválidos",
                request.getDescription(false).replace("uri=", ""));

        // Adiciona detalhes de cada campo com erro
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addFieldError(
                    fieldError.getField(),
                    fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "null",
                    fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata erros de validação customizada de boleto
     */
    @ExceptionHandler(BoletoValidationException.class)
    public ResponseEntity<ErrorResponse> handleBoletoValidationException(
            BoletoValidationException ex,
            WebRequest request) {

        log.warn("Erro de validação de boleto: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Boleto Validation Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));

        if (ex.getCampo() != null) {
            errorResponse.addFieldError(ex.getCampo(), ex.getValorInvalido(), ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata erros de conexão com Kafka
     */
    @ExceptionHandler(KafkaConnectionException.class)
    public ResponseEntity<ErrorResponse> handleKafkaConnectionException(
            KafkaConnectionException ex,
            WebRequest request) {

        log.error("Erro de conexão com Kafka no tópico {}: {}", ex.getTopico(), ex.getMessage(), ex);

        HttpStatus status = ex.isRecoverable() ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                "Kafka Connection Error",
                ex.isRecoverable() ? "Serviço temporariamente indisponível. Tente novamente em alguns instantes."
                        : "Erro ao processar requisição. Contate o suporte.",
                request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Trata erros de buffer overflow
     */
    @ExceptionHandler(BufferOverflowException.class)
    public ResponseEntity<ErrorResponse> handleBufferOverflowException(
            BufferOverflowException ex,
            WebRequest request) {

        log.error("Buffer overflow: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Buffer Overflow",
                "Sistema sobrecarregado. Tente novamente em alguns instantes.",
                request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    /**
     * Trata erros de JSON malformado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        log.warn("JSON malformado: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON",
                "Formato de JSON inválido. Verifique a estrutura dos dados enviados.",
                request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata exceções genéricas não capturadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        log.error("Erro inesperado: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno do servidor. Contate o suporte.",
                request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
