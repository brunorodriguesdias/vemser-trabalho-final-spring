package br.com.dbc.javamosdecolar.dto;

import br.com.dbc.javamosdecolar.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CompradorRelatorioDTO {

    //COMPRADOR
    private String nomeComprador;
    private String cpfComprador;

    //COMPANHIA
    private String cnpjCompanhia;
    private String nomeCompanhia;

    //VENDA
    private String codigoVenda;
    private LocalDateTime dataVenda;

    //PASSAGEM
    private String codigoPassagem;
    private LocalDateTime dataPartida;
    private LocalDateTime dataChegada;
    private Status status;
    private BigDecimal valor;

    //TRECHO
    private String origem;
    private String destino;
}
