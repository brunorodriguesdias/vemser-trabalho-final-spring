package br.com.dbc.javamosdecolar.entity;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "VENDA")
@SQLDelete(sql = "UPDATE AVIACAO.VENDA v SET v.status = 1 WHERE v.id_venda=?")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_venda")
    @SequenceGenerator(name = "seq_venda", sequenceName = "seq_venda", allocationSize = 1)
    @Column(name = "ID_VENDA")
    private Integer idVenda;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "STATUS")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "DATA")
    private LocalDateTime data;

    @Column(name = "ID_PASSAGEM")
    private Integer idPassagem;

//    @Column(name = "ID_COMPANHIA", insertable = false, updatable = false)
//    private Integer idCompanhia;

    @Column(name = "ID_COMPRADOR", insertable = false, updatable = false)
    private Integer idComprador;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPRADOR", referencedColumnName = "ID_USUARIO")
    private CompradorEntity comprador;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "venda")
    @JoinColumn(name = "ID_PASSAGEM", referencedColumnName = "ID_PASSAGEM", insertable = false, updatable = false)
    private PassagemEntity passagem;

}
