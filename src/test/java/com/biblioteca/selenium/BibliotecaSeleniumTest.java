package com.biblioteca.selenium;

import com.biblioteca.service.BibliotecaService;
import com.biblioteca.test.config.WebDriverConfig;
import com.biblioteca.test.pageobjects.FormularioLivroPage;
import com.biblioteca.test.pageobjects.ListaLivrosPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BibliotecaSeleniumTest {

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

    @Test
    @Order(1)
    @DisplayName("CT01 - Carregamento inicial")
    void testInicial() {
        assertThat(listaPage.isPageLoaded())
                .as("Tabela deve estar visível")
                .isTrue();

        assertThat(driver.getCurrentUrl())
                .contains("lista.html");

        assertThat(listaPage.countLivros())
                .as("Inicialmente não há livros")
                .isEqualTo(0);
    }

    @Test
    @Order(2)
    @DisplayName("CT02 - Navegação")
    void testNavegacao() {
        formularioPage = listaPage.clickNovoLivro();
        assertThat(driver.getCurrentUrl()).contains("formulario.html");

        listaPage = formularioPage.cancelar();
        assertThat(driver.getCurrentUrl()).contains("lista.html");
    }

    @Test
    @Order(3)
    @DisplayName("CT03 - Cadastro Fluxo Positivo")
    void testCadastroPositivo() {
        String titulo = "Teste Livro";
        String autor = "Autor Exemplo";
        String cat = "FICCAO";

        // Navega ao formulário
        formularioPage = listaPage.clickNovoLivro();
        formularioPage
                .preencherTitulo(titulo)
                .preencherAutor(autor)
                .selecionarCategoria(cat)
                .submitForm();

        // Aguarda até 5s o JS redirecionar para lista.html
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("lista.html"));

        // Reconstrói a página de lista após redirecionamento
        listaPage = new ListaLivrosPage(driver);

        // Verifica URL correta
        assertThat(driver.getCurrentUrl())
                .contains("lista.html");

        // Verifica se o livro cadastrado aparece na lista
        assertThat(listaPage.existeLivro(titulo))
                .as("Livro deve aparecer na lista")
                .isTrue();
    }
}
