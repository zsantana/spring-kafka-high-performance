package com.example.boleto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping
    public ResponseEntity<Void> criarBoleto(@RequestBody BoletoDTO boleto) {
        boletoService.registrarBoleto(boleto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/buffer-size")
    public ResponseEntity<Integer> obterBufferSize() {
        int bufferSize = boletoService.obterBufferSize();
        return ResponseEntity.ok(bufferSize);
    }
}