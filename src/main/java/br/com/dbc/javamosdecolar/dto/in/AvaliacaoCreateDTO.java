package br.com.dbc.javamosdecolar.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoCreateDTO {

    @Schema(example = "Bruno Rodrigues")
    @NotBlank(message = "Digite seu nome!")
    private String nome;
    @NotNull
    @Schema(example = "10")
    @Size(min = 0, max = 10, message = "Digite um nota de 0 à 10!")
    private Integer nota;
    @Schema(example = "Muito bom!")
    private String descricao;
}
