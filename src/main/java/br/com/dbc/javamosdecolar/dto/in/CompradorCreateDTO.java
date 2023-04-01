package br.com.dbc.javamosdecolar.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompradorCreateDTO {

    @NotBlank(message = "É necessário informar um Login!")
//    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", message = "Endereço de e-mail inválido")
    @Email(message = "E-mail inválido!")
    @Schema(description = "Login do comprador", example = "bruno.rodrigues@email.com", required = true)
    private String login;

    @NotBlank(message = "É necessário informar uma senha!")
    @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!")
    @Pattern(regexp = "^\\S+(?=\\s*$)", message = "Senha inválida! " +
            "Sugiro que não tenha espaço em branco no começo e ao final da senha.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha do comprador", example = "123456", required = true)
    private String senha;

    @NotBlank(message = "É necessário informar um nome!")
    @Size(min=3, max=50, message = "Nome não atende aos requisitos de 3 à 50 caracteres!")
    @Pattern(regexp = "^[a-zA-ZÀ-ú]+([ ]{1}[a-zA-ZÀ-ú]+)*$", message = "Nome inválido!")
    @Schema(description = "Nome do comprador", example = "Bruno Rodrigues", required = true)
    private String nome;

    @NotBlank(message = "É necessário informar um CPF!")
    @Schema(description = "CPF do comprador", example = "12345678910", required = true)
    @CPF(message = "Informe um CPF válido!")
    @Size(min = 11, max = 11)
    private String cpf;
}
