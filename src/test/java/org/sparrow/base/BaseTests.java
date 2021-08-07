package org.sparrow.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseTests {
    protected static WebDriver driver;
    protected static WebDriverWait wait;

    @BeforeAll
    protected static void beforeAll() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        options.addArguments("--disable-notifications");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/webdriver/chromedriver_92.0.4515.107.exe");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 15, 1000);
    }

    @AfterAll
    protected static void afterAll() {
        driver.quit();
    }
}