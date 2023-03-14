package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USUARIO")
public class CompradorEntity extends UsuarioEntity {

    private Integer idComprador;
    private String cpf;

    public CompradorEntity(Integer idUsuario, String login, String senha,
                           String nome, TipoUsuario tipoUsuario, boolean ativo, String cpf) {
        super(idUsuario, login, senha, nome, tipoUsuario, ativo);
        this.cpf = cpf;
    }
}
