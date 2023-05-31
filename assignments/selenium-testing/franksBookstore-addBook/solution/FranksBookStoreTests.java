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

import java.util.*;
import java.util.stream.*;
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
    public void emptyBookNameTest() {

        WebElement submitButton = driver.findElement(By.id("book_submit_button"));
        submitButton.click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Book name cannot be empty");
    }

    @Test
    public void nonexistingAuthorId() {
        driver.findElement(By.id("book_name")).sendKeys("TestName");
        driver.findElement(By.id("author_id")).sendKeys("5");
        driver.findElement(By.id("book_submit_button")).click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Author with that id does not exist");
    }

    @Test
    public void blockDuplicateBooks() {
        driver.findElement(By.id("book_name")).sendKeys("TestName");
        driver.findElement(By.id("author_id")).sendKeys("1");
        driver.findElement(By.id("book_submit_button")).click();
        driver.findElement(By.id("book_submit_button")).click();

        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("The author already has a book with that name!");
    }

    @Test
    public void addedSuccessTest() {
        // save a book
        driver.findElement(By.id("book_name")).sendKeys("TestName");
        driver.findElement(By.id("author_id")).sendKeys("1");
        driver.findElement(By.id("book_submit_button")).click();

        // verify message displayed
        WebElement alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        WebElement alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Book was successfully added!");

        // save a second book
        driver.findElement(By.id("book_name")).clear();
        driver.findElement(By.id("book_name")).sendKeys("TestName 22");
        driver.findElement(By.id("book_submit_button")).click();

        // verify message displayed
        alert = driver.findElement(By.id("alert"));
        assertThat(alert.isDisplayed()).isTrue();
        alertMsg = driver.findElement(By.id("alert-msg"));
        assertThat(alertMsg.getText()).contains("Book was successfully added!");

        // call search command
        WebElement searchBar = driver.findElement(By.id("search_bar"));
        searchBar.sendKeys("TestName");
        searchBar.sendKeys(Keys.RETURN);
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"book_list\"]/div"));

        List<String> expected = new ArrayList<>(
            List.of("\"TestName\" by: Mauricio Aniche",
                    "\"TestName 22\" by: Mauricio Aniche"));

        assertThat(expected).containsExactlyInAnyOrderElementsOf(
            elements.stream().map(e -> e.getText()).collect(Collectors.toList())
        );
    }

    @AfterEach
    public void close() {
        driver.close();
    }
}
