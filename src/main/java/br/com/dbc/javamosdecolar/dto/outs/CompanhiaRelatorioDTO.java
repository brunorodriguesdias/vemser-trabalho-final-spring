package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanhiaRelatorioDTO {

    //COMPANHIA
    @Schema(example = "Companhia Jovem")
    private String nomeCompanhia;
    @Schema(example = "47.026.248/0001-95")
    private String cnpj;

    //PASSAGEM
    @Schema(example = "fae8d717-f21d-48ea-9f10-a7ff0a2f48ae")
    private String codigoPassagem;
    @Schema(example = "2023-02-17T16:18:00")
    private LocalDateTime dataPartida;
    @Schema(example = "2023-02-18T16:18:00")
    private LocalDateTime dataChegada;
    @Schema(example = "DISPONÍVEL")
    private Status statusDaPassagem;
    @Schema(example = "500")
    private BigDecimal valorDaPassagem;

    //TRECHO
    @Schema(example = "SAL")
    private String origem;
    @Schema(example = "REC")
    private String destino;

    //VENDA
    @Schema(example = "c66f0d6e-de0e-4eb7-b6d2-0f0f6c488c6d")
    private String codigoVenda;
    @Schema(example = "2023-03-16T14:30:00")
    private LocalDateTime dataVenda;
    @Schema(example = "CONCLUIDO")
    private Status statusDaVenda;

    //COMPRADOR
    @Schema(example = "Gabriel de Jesus")
    private String nomeComprador;
    @Schema(example = "04500185699")
    private String cpf;
}
