package br.com.dbc.javamosdecolar.entity;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.math.BigDecimal;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "NUMERO_ASSENTO")
    private Integer numeroAssento;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TIPO_ASSENTO")
    private TipoAssento tipoAssento;

    @Column(name = "ID_VENDA")
    private Integer idVenda;

    @Column(name = "ID_VOO")
    private Integer idVoo;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", insertable = false, updatable = false)
    private VendaEntity venda;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOO", referencedColumnName = "ID_VOO", insertable = false, updatable = false)
    private VooEntity voo;
}
