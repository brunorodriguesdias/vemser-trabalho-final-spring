package br.com.dbc.javamosdecolar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompradorDTO extends CompradorCreateDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "status do usuario", example = "true")
    private Boolean ativo;

    @Schema(example = "1")
    private Integer idUsuario;
}
