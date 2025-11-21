package com.example.boleto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boleto.dto.BoletoDTO;
import com.example.boleto.service.BoletoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping
    public ResponseEntity<Void> criarBoleto(@Valid @RequestBody BoletoDTO boleto) {
        boletoService.registrarBoleto(boleto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/buffer-size")
    public ResponseEntity<Integer> obterBufferSize() {
        int bufferSize = boletoService.obterBufferSize();
        return ResponseEntity.ok(bufferSize);
    }
}