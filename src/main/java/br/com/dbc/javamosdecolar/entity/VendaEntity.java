package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "VENDA")
@SQLDelete(sql = "UPDATE AVIACAO.VENDA v SET v.status = 1 WHERE v.id_venda=?")
@Where(clause = "disponivel = 2")
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
    @Column(name = "ID_PASSAGEM", insertable = false, updatable = false)
    private Integer idPassagem;
    @Column(name = "ID_COMPANHIA", insertable = false, updatable = false)
    private Integer idCompanhia;
    @Column(name = "ID_COMPRADOR", insertable = false, updatable = false)
    private Integer idComprador;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPRADOR", referencedColumnName = "ID_USUARIO")
    private CompradorEntity comprador;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO")
    private CompanhiaEntity companhia;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "venda")
    @JoinColumn(name = "ID_PASSAGEM", referencedColumnName = "ID_PASSAGEM")
    private PassagemEntity passagem;

}
