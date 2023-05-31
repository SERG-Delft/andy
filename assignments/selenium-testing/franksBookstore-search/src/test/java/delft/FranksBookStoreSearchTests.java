package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import static org.assertj.core.api.Assertions.assertThat;

class FranksBookStoreSearchTests {

    private WebDriver driver;

    @BeforeEach
    public void initDriver() {
        driver = CSE1110.getDriver();
    }

    // Write your tests here.

    @AfterEach
    public void close() {
        driver.close();
    }

}
