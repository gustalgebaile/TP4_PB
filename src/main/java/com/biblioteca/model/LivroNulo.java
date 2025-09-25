package com.biblioteca.model;

public final class LivroNulo extends Livro {
    public static final LivroNulo INSTANCE = new LivroNulo();

    private LivroNulo() {
        super("N/A", "N/A", Categoria.NAO_CLASSIFICADO);
    }
}
