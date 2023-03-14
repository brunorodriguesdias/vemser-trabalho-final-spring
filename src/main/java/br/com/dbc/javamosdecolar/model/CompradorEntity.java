package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "COMPRADOR")
public class CompradorEntity extends UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_comprador")
    @SequenceGenerator(name = "seq_comprador", sequenceName = "seq_comprador", allocationSize = 1)
    private Integer idComprador;
    @Column(name = "cpf")
    private String cpf;

    public CompradorEntity(Integer idUsuario, String login, String senha,
                           String nome, TipoUsuario tipoUsuario, boolean ativo, String cpf) {
        super(idUsuario, login, senha, nome, tipoUsuario, ativo);
        this.cpf = cpf;
    }
}
