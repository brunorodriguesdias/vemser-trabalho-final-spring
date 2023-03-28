package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDTO extends AvaliacaoCreateDTO {

    private String idAvaliacao;

}
