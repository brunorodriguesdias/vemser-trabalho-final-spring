package br.com.dbc.javamosdecolar.dto;

import br.com.dbc.javamosdecolar.model.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompradorDTO extends CompradorCreateDTO {

    @Schema(description = "status do usuario", example = "true")
    private Boolean ativo;
}
