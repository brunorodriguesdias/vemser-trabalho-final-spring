package br.com.dbc.javamosdecolar.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanhiaUpdateDTO {

    @NotBlank(message = "É necessário informar um nome fantasia!")
    @Size(min=3, max=50, message = "Nome fantasia não atende aos requisitos de 3 à 50 caracteres!")
    @Pattern(regexp = "^[a-zA-ZÀ-ú]+([ ]{1}[a-zA-ZÀ-ú]+)*$", message = "Nome inválido")
    @Schema(description = "Nome fantasia da companhia", example = "Pássaro linhas aéreas", required = true)
    private String nomeFantasia;

    @NotBlank(message = "É necessário informar uma senha!")
    @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!")
    @Schema(description = "Senha de acesso", example = "123456", required = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
}
