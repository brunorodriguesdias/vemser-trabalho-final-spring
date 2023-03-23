package br.com.dbc.javamosdecolar.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AviaoCreateDTO {
    @NotBlank
    @Schema(description = "ID da companhia", example = "5")
    private Integer idCompanhia;

    @NotBlank
    @Schema(description = "Código do avião", example = "")
    private String codigoAviao;

    @NotBlank
    @Size(min = 1, max = 3)
    @Schema(description = "Capacidade do avião", example = "175")
    private Integer capacidade;

    @NotBlank
    @Schema(description = "Data da ultima manutenção", example = "2018-01-07")
    private LocalDate ultimaManutencao;
}
