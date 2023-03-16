package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "COMPRADOR")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class CompradorEntity extends UsuarioEntity {

    @Column(name = "cpf")
    private String cpf;
}
