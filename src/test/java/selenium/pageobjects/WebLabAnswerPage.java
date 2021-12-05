package selenium.pageobjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class WebLabAnswerPage extends BasePageObject {

    public WebLabAnswerPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/div/div[3]/div[1]/div/div/div[1]")
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/div/div[3]/div[1]/div/div/a[1]")
    private WebElement unlockAnswerButton;

    public String getSolution() {
        this.awaitPageLoaded();

        boolean unlockAnswerButtonExists = false;
        try {
            this.unlockAnswerButton.click();
            this.awaitElementVisibility(this.solutionDiv);
        } catch (NoSuchElementException ex) {
            // ignore, button is not shown, answer is already unlocked
        }

        this.solutionDiv.click();
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("a");
        actions.sendKeys("c");
        actions.keyUp(Keys.CONTROL);
        actions.perform();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String solution;
        try {
            solution = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (IOException | UnsupportedFlavorException e) {
            // should never happen
            e.printStackTrace();
            solution = null;
        }

        return solution;
    }

    private void awaitPageLoaded() {
        waitUntil(30, () -> {
            boolean found = false;

            try {
                if (solutionDiv.isDisplayed()) found = true;
            } catch (NoSuchElementException e) {
                // ignore, solution div does not exist
            }

            try {
                if (unlockAnswerButton.isDisplayed()) found = true;
            } catch (NoSuchElementException e) {
                // ignore, unlock button does not exist
            }

            return found;
        });
    }
}
