package br.com.dbc.javamosdecolar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanhiaCreateDTO {

    @NotBlank(message = "É necessário informar um login!")
    @Email(message = "Email inválido!")
    @Schema(description = "Login de acesso", example = "companhia.aviao@email.com", required = true)
    private String login;

    @NotBlank(message = "É necessário informar uma senha!")
    @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!")
    @Schema(description = "Senha de acesso", example = "123456", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotBlank(message = "É necessário informar um nome!")
    @Size(min=3, max=50, message = "Nome não atende aos requisitos de 3 à 50 caracteres!")
    @Schema(description = "Razão social da companhia", example = "Companhia aérea ltda", required = true)
    private String nome;

    @NotBlank(message = "É necessário informar um nome fantasia!")
    @Size(min=3, max=50, message = "Nome fantasia não atende aos requisitos de 3 à 50 caracteres!")
    @Schema(description = "Nome fantasia da companhia", example = "Pássaro linhas aéreas", required = true)
    private String nomeFantasia;

    @NotBlank(message = "É necessário informar um CNPJ!")
    @CNPJ(message = "CNPJ Inválido!")
    @Schema(description = "CNPJ da companhia", example = "29.406.616/0001-49", required = true)
    private String cnpj;
}
