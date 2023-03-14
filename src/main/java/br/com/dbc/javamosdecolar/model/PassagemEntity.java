package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PASSAGEM")
public class PassagemEntity {
    private int idPassagem;
    private String codigo;
    private LocalDateTime dataPartida;
    private LocalDateTime dataChegada;
    private boolean disponivel;
    private BigDecimal valor;
    private TrechoEntity trecho;

    public PassagemEntity(String codigo, LocalDateTime dataPartida, LocalDateTime dataChegada,
                          TrechoEntity trecho, boolean disponivel, BigDecimal valor) {
        this.codigo = codigo;
        this.dataPartida = dataPartida;
        this.dataChegada = dataChegada;
        this.trecho = trecho;
        this.disponivel = disponivel;
        this.valor = valor;
    }

    public PassagemEntity(LocalDateTime dataPartida, LocalDateTime dataChegada, BigDecimal valor) {
        this.dataPartida = dataPartida;
        this.dataChegada = dataChegada;
        this.valor = valor;
        this.disponivel = true;
    }

    public PassagemEntity(String codigo, LocalDateTime dataPartida, LocalDateTime dataChegada,
                          boolean disponivel, BigDecimal valor) {
        this.codigo = codigo;
        this.dataPartida = dataPartida;
        this.dataChegada = dataChegada;
        this.disponivel = disponivel;
        this.valor = valor;
    }
}
