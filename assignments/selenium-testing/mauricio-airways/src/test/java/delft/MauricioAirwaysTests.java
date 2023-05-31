package delft;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;

class MauricioAirwaysTests {

    private WebDriver driver;

    @BeforeEach
    public void initDriver() {
        // Do not change this line. If you change it, your tests will never work, 
        // and your final grade for this exercise will be zero.
        driver = CSE1110.createDriver();
    }
    
    // Write your tests here


    @AfterEach
    public void close() {
        driver.close();
    }

}
