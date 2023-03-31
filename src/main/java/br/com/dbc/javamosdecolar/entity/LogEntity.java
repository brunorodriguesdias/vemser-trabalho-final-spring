package br.com.dbc.javamosdecolar.entity;


import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "log")
public class LogEntity {

    @Id
    private ObjectId idLog;
    private Integer idUsuario;
    private String login;
    private String descricao;
    private TipoOperacao tipoOperacao;
}
