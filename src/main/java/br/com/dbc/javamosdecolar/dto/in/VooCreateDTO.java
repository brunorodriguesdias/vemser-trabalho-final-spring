package br.com.dbc.javamosdecolar.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VooCreateDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    @Schema(description = "Origem do voo", example = "Porto Alegre", required = true)
    private String origem;

    @NotBlank
    @Size(min = 3, max = 30)
    @Schema(description = "Destino do voo", example = "Salvador", required = true)
    private String destino;

    @NotNull
    @Schema(description = "Data de partida do voo", example = "2023-10-10T16:11:26.2", required = true)
    private LocalDateTime dataPartida;

    @NotNull
    @Schema(description = "Data de partida do voo", example = "2023-11-10T16:11:26.2", required = true)
    private LocalDateTime dataChegada;

    @NotNull
    @Positive
    @Schema(description = "Quantidade de assentos disponiveis", example = "20", required = true)
    private Integer assentosDisponiveis;

    @NotNull
    @Schema(description = "ID do avião que fará o voo", example = "3",required = true)
    private Integer idAviao;
}
