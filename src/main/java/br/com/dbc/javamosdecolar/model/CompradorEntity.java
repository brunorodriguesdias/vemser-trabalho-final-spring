package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "cpf")
    private String cpf;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private UsuarioEntity usuarioEntity;
}
