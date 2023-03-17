package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PASSAGEM")
@SQLDelete(sql = "UPDATE AVIACAO.passagem p SET p.disponivel = 0 WHERE p.id_passagem=?")
@Where(clause = "disponivel = 1")
public class PassagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_passagem")
    @SequenceGenerator(name = "seq_passagem", sequenceName = "seq_passagem", allocationSize = 1)
    private Integer idPassagem;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "DATA_PARTIDA")
    private LocalDateTime dataPartida;
    @Column(name = "DATA_CHEGADA")
    private LocalDateTime dataChegada;
    @Column(name = "STATUS")
    private Status status;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRECHO", referencedColumnName = "ID_TRECHO", insertable = false, updatable = false)
    private TrechoEntity trecho;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", insertable = false, updatable = false)
    private VendaEntity venda;

}
