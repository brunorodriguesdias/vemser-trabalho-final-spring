package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRECHO", referencedColumnName = "ID_TRECHO", insertable = false, updatable = false)
    private TrechoEntity trecho;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", insertable = false, updatable = false)
    private VendaEntity venda;

}
