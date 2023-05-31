package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

class FranksBookStoreTests {

    private WebDriver driver;

    @BeforeEach
    public void initDriver() {
        driver = CSE1110.getDriver();
    }

    @Test
    public void emptyNameTest() {

        WebElement submitButton = driver.findElement(By.id("author_submit_button"));
        submitButton.click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Author name cannot be empty");
    }

    @Test 
    public void addedSuccessTest() {

        WebElement authorName = driver.findElement(By.id("author_name"));
        authorName.sendKeys("Mauricio");

        WebElement submitButton = driver.findElement(By.id("author_submit_button"));
        submitButton.click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Author successfully added");

    }

    @Test 
    public void duplicateTest() {
        
        WebElement authorName = driver.findElement(By.id("author_name"));
        authorName.sendKeys("Mauricio");

        WebElement submitButton = driver.findElement(By.id("author_submit_button"));
        submitButton.click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Author successfully added");
        WebElement closeButton = driver.findElements(By.className("closebtn")).get(0);
        closeButton.click();

        // authorName.sendKeys("Mauricio");
        submitButton.click();

        assertThat(alert.isDisplayed()).isTrue();
        assertThat(alertMsg.getText()).contains("Author with that name already exists!");
        
    }

    @AfterEach
    public void close() {
        driver.close();
    }

}
