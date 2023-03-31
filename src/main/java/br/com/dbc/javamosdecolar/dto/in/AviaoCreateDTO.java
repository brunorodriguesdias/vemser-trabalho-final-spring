package br.com.dbc.javamosdecolar.dto.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AviaoCreateDTO {

    @NotBlank
    @Schema(description = "Código do avião", example = "PP-XTY")
    private String codigoAviao;

    @NotNull
    @Min(1)
    @Max(853)
    @Schema(description = "Capacidade do avião", example = "175")
    private Integer capacidade;

    @NotNull
    @PastOrPresent
    @Schema(description = "Data da ultima manutenção", example = "2018-01-07")
    private LocalDate ultimaManutencao;

    @Schema(description = "Identificador da companhia")
    @JsonIgnore
    private Integer idCompanhia;
}
