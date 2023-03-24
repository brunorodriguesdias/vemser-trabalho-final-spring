package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CompradorRelatorioDTO {

    //COMPRADOR
    @Schema(example = "1")
    private Integer idComprador;
    @Schema(example = "Nícolas")
    private String nomeComprador;
    @Schema(example = "07885674855")
    private String cpfComprador;
    @Schema(example = "ATIVO")
    private Boolean statusComprador;

    //VENDA
    @Schema(example = "1")
    private Integer idVenda;
    @Schema(example = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
    private String codigoVenda;
    @Schema(example = "2023-03-24T21:00:34.140Z")
    private LocalDateTime dataVenda;
    @Schema(example = "CONCLUIDA")
    private Status statusDaVenda;

    //PASSAGEM
    @Schema(example = "1")
    private Integer idPassagem;
    @Schema(example = "VENDIDA")
    private Status statusPassagem;
    @Schema(example = "400")
    private BigDecimal valor;
    @Schema(example = "5")
    private Integer numeroAssento;
    @Schema(example = "ECONOMICO")
    private TipoAssento tipoAssento;

    //COMPANHIA
    @Schema(example = "1")
    private Integer idCompanhia;
    @Schema(example = "TAM")
    private String nomeCompanhia;
    @Schema(example = "47.026.248/0001-95")
    private String cnpj;
    @Schema(example = "ATIVO")
    private Boolean statusCompanhia;

    //VOO
    @Schema(example = "1")
    private Integer idVoo;
    @Schema(example = "Salvador")
    private String origem;
    @Schema(example = "Porto Alegre")
    private String destino;
    @Schema(example = "2023-03-24T21:00:34.140Z")
    private LocalDateTime dataPartida;
    @Schema(example = "2023-03-24T21:00:34.140Z")
    private LocalDateTime dataChegada;
    @Schema(example = "DISPONÍVEL")
    private Status statusDoVoo;


}
