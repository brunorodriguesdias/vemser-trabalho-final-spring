package br.com.dbc.javamosdecolar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
<<<<<<< HEAD
import lombok.NoArgsConstructor;
=======
>>>>>>> eb73a22 (wip)

import java.util.List;

@Data
<<<<<<< HEAD
@NoArgsConstructor
=======
>>>>>>> eb73a22 (wip)
@AllArgsConstructor
public class PageDTO<T>{
    private Long totalElementos;
    private Integer quantidadePaginas;
    private Integer pagina;
<<<<<<< HEAD
    private Integer tamanho;
=======
    private Integer taamanho;
>>>>>>> eb73a22 (wip)
    private List<T> elementos;
}
