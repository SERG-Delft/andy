package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import static org.assertj.core.api.Assertions.assertThat;

class FranksBookStoreSearchTests {

    private static WebDriver driver;

    @BeforeAll
    public static void initDriver() {
        driver = CSE1110.getDriver();
    }

    @Test
    public void searchTest() {
        WebElement searchBar = driver.findElement(By.id("search_bar"));
        searchBar.sendKeys("Mauricio");
        searchBar.sendKeys(Keys.RETURN);

        WebElement authorList = driver.findElement(By.id("author_list"));
        WebElement firstResult = authorList.findElement(By.xpath("./div[1]"));
        assertThat(firstResult.getText()).contains("Mauricio");
    }

    @Test
    public void noBookWithNameTest() {
        WebElement searchBar = driver.findElement(By.id("search_bar"));
        searchBar.sendKeys("Not a book");
        searchBar.sendKeys(Keys.RETURN);

        WebElement authorList = driver.findElement(By.id("book_list"));
        WebElement firstResult = authorList.findElement(By.xpath("./div[1]"));
        assertThat(firstResult.getText()).contains("No matching books were found!");
    }


    @AfterAll
    public static void close() {
        driver.close();
    }

}
