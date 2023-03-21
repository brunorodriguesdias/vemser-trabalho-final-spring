package br.com.dbc.javamosdecolar.entity;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PASSAGEM")
@SQLDelete(sql = "UPDATE AVIACAO.passagem p SET p.status = 1 WHERE p.id_passagem=?")
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

    @Column(name = "NUMERO_ASSENTO")
    private String numeroAssento;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TIPO_ASSENTO")
    private TipoAssento tipoAssento;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "DESTINO")
    private String destino;

    @Column(name = "ID_VENDA")
    private Integer idVenda;

    @Column(name = "ID_COMPANHIA")
    private Integer idCompanhia;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    private CompanhiaEntity companhia;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", insertable = false)
    private VendaEntity venda;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AVIAO", referencedColumnName = "ID_AVIAO",
            insertable = false)
    private AviaoEntity aviao;
}
