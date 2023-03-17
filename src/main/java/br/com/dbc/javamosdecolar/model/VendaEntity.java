package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "VENDA")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_venda")
    @SequenceGenerator(name = "seq_venda", sequenceName = "seq_venda", allocationSize = 1)
    @Column(name = "ID_VENDA")
    private int idVenda;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "STATUS")
    private Status status;
    @Column(name = "DATA")
    private LocalDateTime data;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPRADOR", referencedColumnName = "ID_USUARIO")
    private CompradorEntity comprador;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO")
    private CompanhiaEntity companhia;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "venda")
    private Set<PassagemEntity> passagem;

}
