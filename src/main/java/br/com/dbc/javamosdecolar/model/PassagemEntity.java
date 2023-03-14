package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PASSAGEM")
public class PassagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_passagem")
    @SequenceGenerator(name = "seq_passagem", sequenceName = "seq_passagem", allocationSize = 1)
    private int idPassagem;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "DATA_PARTIDA")
    private LocalDateTime dataPartida;
    @Column(name = "DATA_CHEGADA")
    private LocalDateTime dataChegada;
    private boolean disponivel;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "TRECHO")
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
