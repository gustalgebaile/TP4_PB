package com.biblioteca.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void testEtiquetas() {
        assertEquals("Ficção", Categoria.FICCAO.etiqueta());
        assertEquals("Ciência", Categoria.CIENCIA.etiqueta());
        assertEquals("História", Categoria.HISTORIA.etiqueta());
        assertEquals("Tecnologia", Categoria.TECNOLOGIA.etiqueta());
        assertEquals("Romance", Categoria.ROMANCE.etiqueta());
        assertEquals("Não classificado", Categoria.NAO_CLASSIFICADO.etiqueta());
    }
}
