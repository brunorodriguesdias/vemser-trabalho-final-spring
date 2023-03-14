package br.com.dbc.javamosdecolar.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Venda {
    private int idVenda;
    private String codigo;
    private Status status;
    private LocalDateTime data;
    private Comprador comprador;
    private CompanhiaEntity companhiaEntity;
    private Passagem passagem;

    public Venda(String codigo, Passagem passagem, Comprador comprador,
                 CompanhiaEntity companhiaEntity, LocalDateTime data, Status status) {
        this.codigo = codigo;
        this.passagem = passagem;
        this.comprador = comprador;
        this.companhiaEntity = companhiaEntity;
        this.data = data;
        this.status = status;
    }
}
