package br.com.dbc.javamosdecolar.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {
    @NotNull
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            message = "Endereço de e-mail inválido")
    @Schema(description = "Login de acesso ao sistema", example = "example@dbccompany.com.br")
    private String login;

    @NotNull
    @Pattern(regexp = "^\\S+(?=\\s*$)", message = "Senha inválida!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha de acesso ao sistema", example = "123456")
    private String senha;

    @NotNull
    @Schema(description = "Nome do usuário", example = "Maria da Silva")
    private String nome;
}
