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
@SQLDelete(sql = "UPDATE AVIACAO.passagem p SET p.status = 1 WHERE p.id_passagem=?")
//@Where(clause = "status = 2 or status = 3")
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
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private Status status;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "ID_TRECHO")
    private Integer idTrecho;

    @Column(name = "ID_COMPANHIA")
    private Integer idCompanhia;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    private CompanhiaEntity companhia;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", insertable = false, updatable = false)
    private VendaEntity venda;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRECHO", referencedColumnName = "ID_TRECHO", insertable = false, updatable = false)
    private TrechoEntity trecho;

}
