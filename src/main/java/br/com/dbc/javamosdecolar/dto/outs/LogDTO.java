package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {

    @Schema(example = "timestamp: 1680270074, date: 2023-03-31T13:41:14.000+00:00")
    private ObjectId idLog;

    @Schema(example = "10")
    private Integer idUsuario;

    @Schema(example = "email@email.com")
    private String login;

    @Schema(example = "CompanhiaEntity")
    private String descricao;

    @Schema(example = "CRIAR")
    private TipoOperacao tipoOperacao;
}
