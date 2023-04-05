package br.com.dbc.javamosdecolar.service;


import br.com.dbc.javamosdecolar.dto.EmailUsuarioDTO;
import br.com.dbc.javamosdecolar.dto.VendaEmailDTO;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {
    @Value(value = "${kafka.topic}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void enviarEmailUsuario(UsuarioEntity usuarioEntity) throws JsonProcessingException {
        EmailUsuarioDTO emailUsuarioDTO = new EmailUsuarioDTO();
        emailUsuarioDTO.setEmail(usuarioEntity.getLogin());
        emailUsuarioDTO.setNome(usuarioEntity.getNome());

        String objetoSerializado = objectMapper.writeValueAsString(emailUsuarioDTO);
        send(objetoSerializado, 0);
    }

    public void enviarEmailVenda(VendaEntity venda, String acao) throws JsonProcessingException {
        VendaEmailDTO vendaEmailDTO = new VendaEmailDTO();
        vendaEmailDTO.setCodigoVenda(vendaEmailDTO.getCodigoVenda());
        vendaEmailDTO.setAcao(acao);

        EmailUsuarioDTO emailUsuarioDTO = new EmailUsuarioDTO();
        emailUsuarioDTO.setEmail(venda.getComprador().getLogin());
        emailUsuarioDTO.setNome(venda.getComprador().getNome());

        String objetoSerializado = objectMapper.writeValueAsString(emailUsuarioDTO);
        send(objetoSerializado, 1);
    }

    private void send(String mensagem, Integer particao) {
        MessageBuilder<String> stringMessageBuilder = MessageBuilder.withPayload(mensagem)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString());

        if (particao != null) {
            stringMessageBuilder
                    .setHeader(KafkaHeaders.PARTITION_ID, particao);
        }

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(stringMessageBuilder.build());
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult result) {
                log.info(" Log enviado para o kafka com o texto: {} ", mensagem);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error(" Erro ao publicar duvida no kafka com a mensagem: {}", mensagem, ex);
            }
        });
    }
}
