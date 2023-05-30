package delft;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;

class MauricioAirwaysTests {

    private WebDriver driver;

    @BeforeEach
    public void initDriver() {
        driver = CSE1110.createDriver();
    }

    @Test
    public void bookingTest() {
        WebElement flightsList = driver.findElement(By.id("flightList"));
        WebElement firstFlight = flightsList.findElement(By.xpath("./div[1]"));
        firstFlight.click();

        WebElement inputField = driver.findElement(By.className("input_field"));
        inputField.sendKeys("3");

        WebElement bookButton = driver.findElement(By.id("bookButton"));
        bookButton.click();

        WebElement ticketList = driver.findElement(By.id("ticketList"));
        WebElement seatCount = ticketList.findElement(By.xpath("./tr/td[4]"));
        assertThat(seatCount.getText()).isEqualTo("3");
    }

    @Test
    public void bookingNotEnoughSeatsAvailableTest() {
        WebElement flightsList = driver.findElement(By.id("flightList"));
        WebElement singleSpotFlight = flightsList.findElement(By.xpath("./div[3]"));
        singleSpotFlight.click();

        WebElement inputField = driver.findElement(By.className("input_field"));
        inputField.sendKeys("2");

        WebElement bookButton = driver.findElement(By.id("bookButton"));
        bookButton.click();

        WebElement alertMessage = driver.findElement(By.id("alert-msg"));
        assertThat(alertMessage.getText()).isEqualTo("Selected number of seats not possible.");
    }

    @Test
    public void planeAvailabilityUpdatesCorrectly() {
        WebElement flightsList = driver.findElement(By.id("flightList"));
        WebElement firstFlight = flightsList.findElement(By.xpath("./div[1]"));
        firstFlight.click();

        int bookedSeats = 3;
        int available = Integer.parseInt(driver.findElement(By.id("availableSeats")).getText());

        WebElement inputField = driver.findElement(By.className("input_field"));
        inputField.sendKeys(String.valueOf(bookedSeats));

        WebElement bookButton = driver.findElement(By.id("bookButton"));
        bookButton.click();

        assertThat(available - bookedSeats).isEqualTo(Integer.parseInt(driver.findElement(By.id("availableSeats")).getText()));
    }

    @Test
    public void testCorrectFlightIsBooked() {
        WebElement flightsList = driver.findElement(By.id("flightList"));
        WebElement firstFlight = flightsList.findElement(By.xpath("./div[1]"));
        firstFlight.click();

        String selectedFlight = driver.findElement(By.id("selectedFlightName")).getText();

        WebElement inputField = driver.findElement(By.className("input_field"));
        inputField.sendKeys("3");

        WebElement bookButton = driver.findElement(By.id("bookButton"));
        bookButton.click();

        WebElement ticketList = driver.findElement(By.id("ticketList"));
        String bookedFlight = ticketList.findElement(By.xpath("./tr/td[1]")).getText();

        assertThat(bookedFlight).isEqualTo(selectedFlight);
    }

    @Test
    public void bookAllSeatsOnPlane() {
        WebElement flightsList = driver.findElement(By.id("flightList"));
        WebElement firstFlight = flightsList.findElement(By.xpath("./div[1]"));
        firstFlight.click();

        int available = Integer.parseInt(driver.findElement(By.id("availableSeats")).getText());

        WebElement inputField = driver.findElement(By.className("input_field"));
        inputField.sendKeys(String.valueOf(available));

        WebElement bookButton = driver.findElement(By.id("bookButton"));
        bookButton.click();

        assertThat(0).isEqualTo(Integer.parseInt(driver.findElement(By.id("availableSeats")).getText()));

        WebElement ticketList = driver.findElement(By.id("ticketList"));
        WebElement seatCount = ticketList.findElement(By.xpath("./tr/td[4]"));
        assertThat(Integer.parseInt(seatCount.getText())).isEqualTo(available);
    }

    @AfterEach
    public void close() {
        driver.close();
    }

}

