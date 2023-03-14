package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "VENDA")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_venda")
    @SequenceGenerator(name = "seq_venda", sequenceName = "seq_venda", allocationSize = 1)
    private int idVenda;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "STATUS")
    private Status status;
    @Column(name = "DATA")
    private LocalDateTime data;
    private CompradorEntity comprador;
    private CompanhiaEntity companhiaEntity;
    private PassagemEntity passagem;

    public VendaEntity(String codigo, PassagemEntity passagem, CompradorEntity comprador,
                       CompanhiaEntity companhiaEntity, LocalDateTime data, Status status) {
        this.codigo = codigo;
        this.passagem = passagem;
        this.comprador = comprador;
        this.companhiaEntity = companhiaEntity;
        this.data = data;
        this.status = status;
    }
}
