package com.biblioteca.model;

public enum Categoria {
    FICCAO,
    CIENCIA,
    HISTORIA,
    TECNOLOGIA,
    NAO_CLASSIFICADO,
    ROMANCE;

    public String etiqueta() {
        return switch (this) {
            case FICCAO -> "Ficção";
            case CIENCIA -> "Ciência";
            case HISTORIA -> "História";
            case TECNOLOGIA -> "Tecnologia";
            case ROMANCE -> "Romance";
            case NAO_CLASSIFICADO -> "Não classificado";
        };
    }
}
