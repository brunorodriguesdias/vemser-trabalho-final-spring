package br.com.dbc.javamosdecolar.dto.outs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {
    @NotNull
    @Schema(description = "Login de acesso ao sistema", example = "example@dbccompany.com.br")
    private String login;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha de acesso ao sistema", example = "123456")
    private String senha;
}
