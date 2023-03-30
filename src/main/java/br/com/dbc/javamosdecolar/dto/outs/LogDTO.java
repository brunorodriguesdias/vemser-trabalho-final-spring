package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {

    private String idLog;
    private Integer idUsuario;
    private String login;
    private String descricao;
    private TipoOperacao tipoOperacao;
}
