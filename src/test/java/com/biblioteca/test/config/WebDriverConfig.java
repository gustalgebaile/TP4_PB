package com.biblioteca.test.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

@TestConfiguration
public class WebDriverConfig {

    private static final int DEFAULT_TIMEOUT_SECONDS = 10;
    private static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public enum BrowserType {
        CHROME, FIREFOX, EDGE
    }

    @Bean
    @Primary
    public WebDriver webDriver() {
        String browserProperty = System.getProperty("selenium.browser", "chrome");
        boolean headless = Boolean.parseBoolean(System.getProperty("selenium.headless", "true"));

        BrowserType browserType = BrowserType.valueOf(browserProperty.toUpperCase());
        return createWebDriver(browserType, headless);
    }

    @Bean
    public WebDriverWait webDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    public WebDriver createWebDriver(BrowserType browserType, boolean headless) {
        return switch (browserType) {
            case CHROME -> createChromeDriver(headless);
            case FIREFOX -> createFirefoxDriver(headless);
            default -> throw new IllegalArgumentException("Navegador não suportado: " + browserType);
        };
    }

    private WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Otimizações para testes automatizados
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--disable-plugins",
                "--disable-images",
                "--disable-javascript-harmony-shipping",
                "--disable-background-timer-throttling",
                "--disable-background-networking",
                "--disable-client-side-phishing-detection",
                "--disable-sync",
                "--metrics-recording-only",
                "--no-first-run",
                "--safebrowsing-disable-auto-update",
                "--enable-automation",
                "--password-store=basic",
                "--use-mock-keychain",
                "--window-size=1920,1080"
        );

        // Configurações de performance
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        ChromeDriver driver = new ChromeDriver(options);
        configureTimeouts(driver);

        return driver;
    }

    private WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        // Otimizações para testes automatizados
        options.addArguments("--width=1920", "--height=1080");
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");

        FirefoxDriver driver = new FirefoxDriver(options);
        configureTimeouts(driver);

        return driver;
    }

    private void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    public static class CustomWebDriverConfig {

        public static WebDriver createPerformanceTestDriver() {
            WebDriverConfig config = new WebDriverConfig();
            WebDriver driver = config.createChromeDriver(true);

            // Timeouts estendidos para testes de performance
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

            return driver;
        }

        public static WebDriver createVisualTestDriver() {
            WebDriverConfig config = new WebDriverConfig();
            return config.createChromeDriver(false); // Nunca headless para testes visuais
        }
    }
}