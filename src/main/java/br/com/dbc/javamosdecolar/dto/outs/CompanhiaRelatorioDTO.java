package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanhiaRelatorioDTO {

    //COMPANHIA
    @Schema(example = "1")
    private Integer idUsuario;
    @Schema(example = "Companhia Jovem")
    private String nome;
    @Schema(example = "47.026.248/0001-95")
    private String cnpj;

    //AVIAO
    @Schema(example = "1")
    private Integer idAviao;
    @Schema(example = "XXXX-XXXXXXXXX-XXXXXXX-XXXXXXX")
    private String codigoAviao;
    @Schema(example = "500")
    private Integer capacidade;
    @Schema(example = "2023-03-24")
    private LocalDate ultimaManutencao;
    @Schema(example = "ATIVO")
    private boolean ativo;

    //VOO
    @Schema(example = "1")
    private Integer idVoo;
    @Schema(example = "Salvador")
    private String origem;
    @Schema(example = "Porto Alegre")
    private String destino;
    @Schema(example = "2023-03-24T15:14:33.871Z")
    private LocalDateTime dataPartida;
    @Schema(example = "2023-03-24T17:14:33.871Z")
    private LocalDateTime dataChegada;
    @Schema(example = "DISPONIVEL")
    private Status status;
    @Schema(example = "50")
    private Integer assentosDisponiveis;

    //PASSAGEM
    @Schema(example = "10")
    private Object qtdDePassagens;
}
