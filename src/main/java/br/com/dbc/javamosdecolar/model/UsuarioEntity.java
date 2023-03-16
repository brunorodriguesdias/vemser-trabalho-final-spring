package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    private Integer idUsuario;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TIPO")
    private TipoUsuario tipoUsuario;

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo = true;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuarioEntity")
    private CompradorEntity compradorEntity;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuarioEntity")
    private CompanhiaEntity companhiaEntity;
}