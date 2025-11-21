package com.example.boleto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para representar um boleto bancário com validações
 */
public record BoletoDTO(

        @NotBlank(message = "Nome do pagador é obrigatório") @Size(min = 3, max = 100, message = "Nome do pagador deve ter entre 3 e 100 caracteres") String pagador,

        @NotBlank(message = "Documento (CPF/CNPJ) é obrigatório") @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "Documento deve ser um CPF (11 dígitos) ou CNPJ (14 dígitos) válido") String documento,

        @NotNull(message = "Valor do boleto é obrigatório") @DecimalMin(value = "0.01", message = "Valor do boleto deve ser maior que zero") BigDecimal valor,

        @NotNull(message = "Data de vencimento é obrigatória") @FutureOrPresent(message = "Data de vencimento não pode ser no passado") LocalDate dataVencimento,

        // @NotBlank(message = "Código de barras é obrigatório") @Pattern(regexp = "^\\d{47}$|^[\\d.\\s-]{47,54}$", message = "Código de barras deve ter formato válido (47 dígitos)") 
        String codigoBarras) {
}
