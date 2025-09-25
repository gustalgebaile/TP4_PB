package com.biblioteca.selenium;

import com.biblioteca.model.Categoria;
import com.biblioteca.service.BibliotecaService;
import com.biblioteca.test.config.WebDriverConfig;
import com.biblioteca.test.pageobjects.FormularioLivroPage;
import com.biblioteca.test.pageobjects.ListaLivrosPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BibliotecaParametrizedTest {

    private WebDriver driver;
    private ListaLivrosPage listaPage;
    private FormularioLivroPage formularioPage;

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private BibliotecaService bibliotecaService;

    @BeforeEach
    void setUp() {
        bibliotecaService.limparBase();
        driver = new WebDriverConfig().webDriver();
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl + "/lista.html");
        listaPage = new ListaLivrosPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @ParameterizedTest(name = "Categoria {0}")
    @EnumSource(Categoria.class)
    @Order(1)
    void testCadastroCategorias(Categoria categoria) {
        String titulo = "Livro" + categoria.name();
        formularioPage = listaPage.clickNovoLivro();
        formularioPage
                .preencherTitulo(titulo)
                .preencherAutor("Autor")
                .selecionarCategoria(categoria.name())
                .submitForm();

        assertThat(listaPage.existeLivro(titulo))
                .as("Deve cadastrar categoria " + categoria)
                .isTrue();
    }

    @ParameterizedTest(name = "Tamanho {0}")
    @ValueSource(ints = {1, 50, 100})
    @Order(2)
    void testBoundary(int size) {
        String txt = "a".repeat(size);
        formularioPage = listaPage.clickNovoLivro();
        formularioPage
                .preencherTitulo(txt)
                .preencherAutor("Autor")
                .selecionarCategoria("FICCAO");

        if (size <= 100) {
            listaPage = formularioPage.submitForm();

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains("lista.html"));

            listaPage = new ListaLivrosPage(driver);

            assertThat(listaPage.existeLivro(txt)).isTrue();

        } else {
            formularioPage.submitForm();

            // Aguarda que a mensagem de erro "Preencha todos os campos!" fique visível
            WebElement msgElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));

            assertThat(msgElement.getText())
                    .contains("Preencha todos os campos!");

            // A URL deve permanecer no formulário
            assertThat(driver.getCurrentUrl()).contains("formulario.html");
        }
    }
}