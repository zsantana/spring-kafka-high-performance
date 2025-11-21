package com.example.boleto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.boleto.dto.BoletoDTO;

@Service
public class BoletoService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BoletoService.class); 

    private final KafkaTemplate<String, BoletoDTO> kafkaTemplate;
    private static final String TOPIC = "boletos-registro";

    private final Queue<BoletoDTO> buffer = new ConcurrentLinkedQueue<>();
    private static final int BATCH_SIZE = 10000;

    public BoletoService(KafkaTemplate<String, BoletoDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public int obterBufferSize() {
        return buffer.size();

    }
    
    public void registrarBoleto(BoletoDTO boleto) {
        
        buffer.offer(boleto);

        if (buffer.size() >= BATCH_SIZE) {
            processarLote();
        }

    }

    @Scheduled(fixedDelay = 500) 
    public synchronized void processarLote() {

        // if (buffer.size() < BATCH_SIZE) return;
        if (buffer.isEmpty()) return;

        List<BoletoDTO> lote = new ArrayList<>();
        // Drena até 1000 itens da fila
        for (int i = 0; i < BATCH_SIZE; i++) {
            BoletoDTO b = buffer.poll();
            if (b == null) break;
            lote.add(b);
        }

        if (lote.isEmpty()) return;

        try {
            // 1. BULK INSERT (Muito mais rápido que insert unitário)
            //boletoRepository.saveAllBatch(lote); 
            
            // 2. Envia para o Kafka (O client do Kafka vai agrupar isso na rede)
            // Usamos virtual threads aqui para disparar os sends em paralelo se quiser
            log.info("### Processando lote de {} boletos", lote.size());
            lote.forEach(b -> kafkaTemplate.send(TOPIC, b));

        } catch (Exception e) {
            log.error("Erro ao processar lote", e);
            // Lógica de retry ou DLQ manual necessária aqui
        }
    }
}