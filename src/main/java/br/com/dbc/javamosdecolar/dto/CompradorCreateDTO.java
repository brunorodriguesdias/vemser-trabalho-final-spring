package br.com.dbc.javamosdecolar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CompradorCreateDTO {

    @NotBlank(message = "É necessário informar um CPF!")
    @CPF(message = "CPF inválido!")
    @Schema(description = "CPF do comprador", example = "123.456.789.10", required = true)
    private String cpf;

    @NotBlank(message = "É necessário informar um Login!")
    @Email(message = "Email inválido!")
    @Schema(description = "Login do comprador", example = "bruno.rodrigues@email.com", required = true)
    private String login;

    @NotBlank(message = "É necessário informar uma senha!")
    @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha do comprador", example = "123456", required = true)
    private String senha;

    @NotBlank(message = "É necessário informar um nome!")
    @Size(min=3, max=50, message = "Nome não atende aos requisitos de 3 à 50 caracteres!")
    @Schema(description = "Nome do comprador", example = "Bruno Rodrigues", required = true)
    private String nome;
}
