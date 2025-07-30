import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

/*
This test bot is used to verify the search functionality on the Udemy website by entering a keyword and checking the returned course titles.

    Sample Code: Moodle/Se2226/Week7-Lab/Bot.java (Atabarış Hoca's Bot Code in the moodle)

    ! This bot may not work properly at all times due to Cloudflare bot protection used on the official Udemy web application.

    ! For more consistent results, we use DiscUdemyTestBot instead.
    ! DiscUdemyTestBot works on https://www.discudemy.com/, which is an archive-like version of Udemy.

Common issues:
    -> Cloudflare blocks the page with a CAPTCHA
    -> The search bar becomes unusable for automation
    -> The site blocks the connection after several repeated requests
 */


public class udemyTestBot {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actionProvider;
    private final int TIMEOUT = 5;
    private final int DELAY = 2;
    private String URL;
    private ArrayList<String> courseTitles;

    public udemyTestBot(String url) {
        courseTitles = new ArrayList<>();
        if (validateUrl(url)) {
            this.URL = url;
            initializeDriver();
            initializeWait();
            initializeActionProvider();
        }
    }

    private boolean validateUrl(String url) {

        // TODO -> make more advanced before project deadline
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

    private  void connect(){
        driver.get(URL);
    }

    public ArrayList<String> getCourseTitles() {
        return courseTitles;
    }


    public void searchByKeyword(String keyword) {
        search(keyword);
        storeData();
        quit();


        System.out.println("Search KeyWord: " + keyword);
        System.out.println("Number of courses find: " + courseTitles.size());
        for (String title : courseTitles) {
            System.out.println("- " + title);
        }
    }


    public void search(String keyword){
        connect();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        try{
            var searchBox = wait.until(driver -> driver.findElement(By.name("q")));

            searchBox.sendKeys(keyword);
            searchBox.sendKeys(org.openqa.selenium.Keys.ENTER);

            actionProvider.pause(Duration.ofSeconds(DELAY)).build().perform();

        }catch (Exception e){
            System.out.println("Something went wrong during searching! Error: " + e.getMessage());
        }
    }

    public void storeData(){
        courseTitles.clear();
        try {

            var titleElements = wait.until(driver ->
                    driver.findElements(By.cssSelector("[data-purpose='search-course-card-title']"))
            );

            for (WebElement element : titleElements) {
                String title = element.getText().trim();
                if (!title.isEmpty()) {
                    courseTitles.add(title);
                }
            }
            System.out.println("Number of courses: " + courseTitles.size());

        } catch (Exception e) {
            System.out.println("Something went wrong during data storage: " + e.getMessage());
        }
    }


    private void quit() {
        try {
            actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
            driver.quit();
        } catch (Exception e) {
            System.out.println("Something went wrong !" + e.getMessage());
        }
    }

}
