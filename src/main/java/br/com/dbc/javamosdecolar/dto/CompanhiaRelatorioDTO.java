package br.com.dbc.javamosdecolar.dto;

import br.com.dbc.javamosdecolar.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanhiaRelatorioDTO {

    //COMPANHIA
    private String nomeCompanhia;
    private String cnpj;

    //PASSAGEM
    private String codigoPassagem;
    private LocalDateTime dataPartida;
    private LocalDateTime dataChegada;
    private Status statusDaPassagem;
    private BigDecimal valorDaPassagem;

    //TRECHO
    private String origem;
    private String destino;

    //VENDA
    private String codigoVenda;
    private LocalDateTime dataVenda;
    private Status statusDaVenda;

    //COMPRADOR

    private String nomeComprador;
    private String cpf;
}
