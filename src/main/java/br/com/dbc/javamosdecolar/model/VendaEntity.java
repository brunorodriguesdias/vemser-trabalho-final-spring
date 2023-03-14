package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "VENDA")
public class VendaEntity {
    private int idVenda;
    private String codigo;
    private Status status;
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
