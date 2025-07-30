import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/*
 * This is a Selenium-based bot for interacting with the DiscUdemy platform (https://www.discudemy.com).
 * Sample Code: Moodle/Se2226/Week7-Lab/Bot.java (Atabarış Hoca's Bot Code in the moodle)
 *
 * Key functionalities:
 *      1. searchByKeyword(): Searches for courses by a given keyword and stores the course titles.
 *      2. clickCategoryByName(): Clicks a specific category on the site.
 *      3. storeData(): Captures course titles from the search results.
 *
 * The bot uses ChromeDriver, WebDriverWait, and Actions to interact with the page and extract course data.
 *     ---> A working ChromeDriver setup at "drivers/chromedriver-win64/chromedriver.exe".
 */

public class DiscUdemyTestBot {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actionProvider;
    private final int TIMEOUT = 5;
    private final int DELAY = 2;
    private String URL;
    private ArrayList<String> courseTitles;

    public DiscUdemyTestBot(String url) {
        courseTitles = new ArrayList<>();
        if (validateUrl(url)) {
            this.URL = url;
            initializeDriver();
            initializeWait();
            initializeActionProvider();
        }
    }

    private boolean validateUrl(String url) {
        return url != null && url.startsWith("http");
    }

    private void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--whitelisted-ips=''");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
    }

    private void initializeWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    private void initializeActionProvider() {
        actionProvider = new Actions(driver);
    }

    public void connect() {
        driver.get(URL);
    }

    public ArrayList<String> getCourseTitles() {
        return courseTitles;
    }

    public void searchByKeyword(String keyword) {
        search(keyword);
        storeData();
        quit();

        System.out.println("Arama yapılan kelime: " + keyword);
        System.out.println("Bulunan kurs sayısı: " + courseTitles.size());
        for (String title : courseTitles) {
            System.out.println("- " + title);
        }
    }

    public void search(String keyword) {
        connect();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();

        try {
            WebElement searchBox = wait.until(driver -> driver.findElement(By.cssSelector(".searchInput")));
            searchBox.sendKeys(keyword + Keys.ENTER);
            actionProvider.pause(Duration.ofSeconds(DELAY)).build().perform();
        } catch (Exception e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }

    public void clickCategoryByName(String categoryName) {
        try {
            WebElement categoryButton = driver.findElement(
                    By.xpath("//a[contains(@class,'catbtn') and contains(., '" + categoryName + "')]")
            );
            categoryButton.click();
            waitSeconds(2);
        } catch (Exception e) {
            System.out.println("Category '" + categoryName + "' not found or not clickable. " + e.getMessage());
        }
    }


    public void storeData() {
        courseTitles.clear();

        try {
            List<WebElement> titleElements = wait.until(driver ->
                    driver.findElements(By.cssSelector(".content .card-header"))
            );

            for (WebElement element : titleElements) {
                String title = element.getText().trim();
                if (!title.isEmpty()) {
                    courseTitles.add(title);
                }
            }

            System.out.println("Number of courses: " + courseTitles.size());

        } catch (Exception e) {
            System.out.println("Error during course extraction: " + e.getMessage());
        }
    }

    public void waitSeconds(int sec) {
        actionProvider.pause(Duration.ofSeconds(sec)).build().perform();
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public void quit() {
        try {
            actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
            driver.quit();
        } catch (Exception e) {
            System.out.println("Driver close failed: " + e.getMessage());
        }
    }
}
