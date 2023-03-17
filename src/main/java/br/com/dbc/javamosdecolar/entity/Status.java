package br.com.dbc.javamosdecolar.entity;

import java.util.Arrays;

public enum Status {
    CONCLUIDO(1),
    CANCELADO(2),
    DISPONIVEL(3),
    VENDIDA(4);


    private Integer status;

    Status(Integer tipo) {
        this.status = tipo;
    }

    public Integer getTipo() {
        return status;
    }

    public static Status ofTipo(Integer numero) {
        return Arrays.stream(Status.values())
                .filter(tipo -> tipo.getTipo().equals(numero))
                .findFirst()
                .get();
    }
}
