package br.com.dbc.javamosdecolar.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "avaliacao")
public class AvaliacaoEntity {

    @Id
    private Integer idAvaliacao;

    private String nome;

    private Integer nota;

    private String descricao;
}
