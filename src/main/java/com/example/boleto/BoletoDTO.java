package com.example.boleto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BoletoDTO(
    String pagador,
    String documento,
    BigDecimal valor,
    LocalDate dataVencimento,
    String codigoBarras
) {}