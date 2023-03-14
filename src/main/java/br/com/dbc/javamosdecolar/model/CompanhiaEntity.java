package br.com.dbc.javamosdecolar.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "COMPANHIA")
@NoArgsConstructor
public final class CompanhiaEntity extends UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_companhia")
    @SequenceGenerator(name = "seq_companhia", sequenceName = "seq_companhia", allocationSize = 1)
    private Integer idCompanhia;
    private String cnpj;
    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    public CompanhiaEntity(Integer idUsuario, String login, String senha, String nome,
                           TipoUsuario tipoUsuario, boolean ativo,
                           Integer idCompanhia, String cnpj, String nomeFantasia) {
        super(idUsuario, login, senha, nome, tipoUsuario, ativo);
        this.idCompanhia = idCompanhia;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
    }

    public CompanhiaEntity(Integer idUsuario, String login, String senha, String nome,
                           TipoUsuario tipoUsuario, boolean ativo, String cnpj, String nomeFantasia) {
        super(idUsuario, login, senha, nome, tipoUsuario, ativo);
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
    }
}

