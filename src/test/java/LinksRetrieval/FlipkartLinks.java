package LinksRetrieval;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartLinks {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void initializingBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.flipkart.com/");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void findingAllTheAvailableLinks() {
        List<WebElement> links = driver.findElements(By.cssSelector("a"));
        wait.until(ExpectedConditions.visibilityOfAllElements(links));
        for (WebElement link : links) {
            System.out.println("Link: " + link.getAttribute("href"));
        }
    }

    @Test
    public void findingAllTheAvailableLinksByStream() {
        System.out.println("Inside test findingAllTheAvailableLinksByStream...............");
        System.out.println(System.currentTimeMillis());
        driver.findElements(By.tagName("a")).stream().forEach(linkIterator -> {
            System.out.println("Interating values by a stream: " + linkIterator.getAttribute("href"));
        });
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void findingAllTheAvailableLinksByParallelStream() {
        System.out.println("Inside test findingAllTheAvailableLinksByParallelStream...............");
        System.out.println(System.currentTimeMillis());
        driver.findElements(By.tagName("a")).parallelStream().forEach(linkIterator -> {
            System.out.println("Interating values by a parallel stream: " + linkIterator.getAttribute("href"));
        });
        System.out.println(System.currentTimeMillis());
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
