package delft;

import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class TodaysEventsTest {

    private WebDriver driver;

    private WebElement fromField;
    private WebElement toField;
    private WebElement descriptionField;
    private WebElement addEventButton;
    private WebElement alert;

    @BeforeEach
    public void initDriver() {
        // Use this driver in your tests, and do not call `driver.get()`.
        // Otherwise, your tests cannot be graded
        // and your final grade for this exercise will be zero.
        driver = CSE1110.createDriver();

        fromField = driver.findElement(By.id("from"));
        toField = driver.findElement(By.id("to"));
        descriptionField = driver.findElement(By.id("description"));
        addEventButton = driver.findElement(By.id("addEventButton"));
        alert = driver.findElement(By.id("alert-msg"));

        // add test event
        fromField.sendKeys("10:30");
        toField.sendKeys("11:00");
        descriptionField.sendKeys("Test event");
        addEventButton.click();
    }

    @AfterEach
    public void close() {
        // Don't remove
        driver.close();
    }

    @Test
    public void testAddSingleEvent() {
        // The event is already added in the @BeforeEach

        assertThat(alert.isDisplayed()).isFalse();

        assertThat(driver.findElements(By.className("event"))).hasSize(1);
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div/h3")).getText())
                .isEqualTo("Test event");
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div/p")).getText())
                .isEqualTo("10:30 - 11:00");
    }

    @Test
    public void testAddTwoEvents() {
        // The first event is already added in the @BeforeEach

        fromField.clear();
        fromField.sendKeys("11:01");
        toField.clear();
        toField.sendKeys("12:00");
        descriptionField.clear();
        descriptionField.sendKeys("Another event");
        addEventButton.click();

        assertThat(alert.isDisplayed()).isFalse();

        assertThat(driver.findElements(By.className("event"))).hasSize(2);
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/h3")).getText())
                .isEqualTo("Test event");
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div[1]/p")).getText())
                .isEqualTo("10:30 - 11:00");
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/h3")).getText())
                .isEqualTo("Another event");
        assertThat(driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/p")).getText())
                .isEqualTo("11:01 - 12:00");

        // Those assertions can also be performed as follows:
        // var children = driver.findElements(By.className("event"));
        // assertThat(children).hasSize(2);
        // assertThat(children.get(0).findElement(By.tagName("h3")).getText())
        //         .isEqualTo("Test event");
        // assertThat(children.get(0).findElement(By.tagName("p")).getText())
        //         .isEqualTo("10:30 - 11:00");
        // assertThat(children.get(1).findElement(By.tagName("h3")).getText())
        //         .isEqualTo("Another event");
        // assertThat(children.get(1).findElement(By.tagName("p")).getText())
        //         .isEqualTo("11:01 - 12:00");
    }

    @ParameterizedTest
    @CsvSource({
            "10:00, 10:45", // left
            "10:45, 12:00", // right
            "10:00, 12:00", // outer
            "10:40, 10:50", // inner
    })
    public void testOverlap(String from, String to) {
        fromField.clear();
        fromField.sendKeys(from);
        toField.clear();
        toField.sendKeys(to);
        descriptionField.clear();
        descriptionField.sendKeys("Another event");
        addEventButton.click();

        assertThat(alert.isDisplayed()).isTrue();
        assertThat(alert.getText()).isEqualTo("This event should not overlap with an existing event");

        assertThat(driver.findElements(By.className("event"))).hasSize(1);
    }

}
