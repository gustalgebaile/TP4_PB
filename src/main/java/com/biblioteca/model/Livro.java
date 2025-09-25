package com.biblioteca.model;

import java.util.Objects;

public class Livro {
    private final String titulo;
    private final String autor;
    private final Categoria categoria;

    public Livro(final String titulo, final String autor, final Categoria categoria) {
        this.titulo = Objects.requireNonNull(titulo, "Título obrigatório");
        this.autor = Objects.requireNonNull(autor, "Autor obrigatório");
        this.categoria = Objects.requireNonNull(categoria, "Categoria obrigatória");
        if (titulo.isBlank() || autor.isBlank()) {
            throw new IllegalArgumentException("Título e Autor não podem ser em branco");
        }
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public Categoria getCategoria() { return categoria; }

    public String etiquetaCategoria() {
        return categoria.etiqueta();
    }
    @Override
    public String toString() {
        return "%s - %s (%s)".formatted(titulo, autor, categoria.etiqueta());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro other)) return false;
        return titulo.equals(other.titulo)
                && autor.equals(other.autor)
                && categoria == other.categoria;
    }
    @Override
    public int hashCode() {
        return Objects.hash(titulo, autor, categoria);
    }
}
