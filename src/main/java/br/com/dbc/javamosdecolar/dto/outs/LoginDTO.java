package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO extends UsuarioCreateDTO {
    private Integer idUsuario;

    private TipoUsuario tipoUsuario;

    private Boolean ativo;
}
