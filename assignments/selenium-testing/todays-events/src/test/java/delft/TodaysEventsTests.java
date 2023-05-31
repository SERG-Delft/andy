package delft;

import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class TodaysEventsTests {

    private WebDriver driver;

    @BeforeEach
    public void initDriver() {
        // Use this driver in your tests, and do not call `driver.get()`.
        // Otherwise, your tests cannot be graded 
        // and your final grade for this exercise will be zero.
        driver = CSE1110.createDriver();
        
    }
    
    // Write your tests here.


    @AfterEach
    public void close() {
        // Don't remove
        driver.close();
    }
}
