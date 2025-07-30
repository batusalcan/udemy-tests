import org.openqa.selenium.By;
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
 * Coded by Demir Demirdöğen
 * Sample Code: Moodle/Se2226/Week7-Lab/Bot.java (Atabarış Hoca's Bot Code in the moodle)
 *
 *
 * This class is a Selenium-based bot designed for automating and verifying the purchase flow of a shopping cart on a sample e-commerce website.
 * The bot performs the following actions:
 *
 * 1. Login: Logs into the application with a username and password.
 * 2. Inventory Interaction: Captures all available courses in the inventory and allows selection and addition of courses to the shopping cart.
 * 3. Cart Interaction: Adds or removes items from the shopping cart, calculates the total cost, and verifies that the cart and checkout are consistent.
 * 4. Checkout Process: Fills in the checkout form (user details and shipping info), navigates to the checkout page, and verifies the displayed summary.
 * 5. Total Calculation: Verifies if the final total matches the sum of the item prices plus tax.
 *
 * The bot interacts with the following page elements:
 * - Inventory items and their details
 * - Shopping cart (add, remove, and verify)
 * - Checkout form (user details, item total, shipping info, and final total)
 *
 * The bot is intended to test a normal purchase flow:
 * - login -> homepage -> category -> sub-category -> select course -> add to cart -> checkout
 *
 * URL being used for testing: https://www.saucedemo.com
 * Note: This bot assumes that you have a working ChromeDriver set up on your system.
 *     --> "drivers/chromedriver-win64/chromedriver.exe"
 */


public class SauceDemoTestBot {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actionProvider;
    private final int TIMEOUT = 5;
    private final int DELAY = 2;
    private String URL;
    private ArrayList<String> inventoryTitles;
    private ArrayList<String> cartTitles;
    private ArrayList<String> checkoutTitles;

    // Checkout summary fields
    private String paymentInfo;
    private String shippingInfo;
    private String itemTotal;
    private String tax;
    private String total;

    public SauceDemoTestBot(String url) {
        inventoryTitles = new ArrayList<>();
        cartTitles      = new ArrayList<>();
        checkoutTitles  = new ArrayList<>();
        if (validateUrl(url)) {
            this.URL = url;
            initializeDriver();
            initializeWait();
            initializeActionProvider();
        } else {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }

    private boolean validateUrl(String url) {
        return url != null && url.startsWith("http");
    }

    private void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled", "--start-maximized");
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
        sleep(DELAY);
    }

    public void login(String username, String password) {
        connect();
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        sleep(DELAY);
    }

    public void storeInventoryItems() {
        inventoryTitles.clear();
        for (WebElement e : driver.findElements(By.cssSelector(".inventory_item_name"))) {
            String name = e.getText().trim();
            if (!name.isEmpty()) inventoryTitles.add(name);
        }
    }

    public ArrayList<String> getInventoryTitles() {
        return inventoryTitles;
    }

    public void addToCartByIndex(int index) {
        driver.findElement(By.xpath("(//button[text()='Add to cart'])[" + index + "]")).click();
        sleep(DELAY);
    }

    public void removeFromCartInInventoryByIndex(int index) {
        driver.findElement(By.xpath("(//button[text()='Remove'])[" + index + "]")).click();
        sleep(DELAY);
    }

    public void openCart() {
        driver.findElement(By.id("shopping_cart_container")).click();
        sleep(DELAY);
    }

    public void storeCartItems() {
        cartTitles.clear();
        openCart();
        for (WebElement e : driver.findElements(By.cssSelector(".cart_item .inventory_item_name"))) {
            String name = e.getText().trim();
            if (!name.isEmpty()) cartTitles.add(name);
        }
    }

    public ArrayList<String> getCartTitles() {
        return cartTitles;
    }

    public double calculateCartSum() {
        openCart();
        double sum = 0.0;
        for (WebElement priceEl : driver.findElements(By.cssSelector(".cart_item .inventory_item_price"))) {
            String txt = priceEl.getText().replace("$", "");
            sum += Double.parseDouble(txt);
        }
        return sum;
    }

    public void clickCheckout() {
        openCart();
        driver.findElement(By.id("checkout")).click();
        sleep(DELAY);
    }

    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        driver.findElement(By.id("postal-code")).sendKeys(postalCode);
        driver.findElement(By.id("continue")).click();
        sleep(DELAY);
    }

    public void storeCheckoutItems() {
        checkoutTitles.clear();
        for (WebElement e : driver.findElements(By.cssSelector(".cart_item .inventory_item_name"))) {
            String name = e.getText().trim();
            if (!name.isEmpty()) checkoutTitles.add(name);
        }
    }

    public ArrayList<String> getCheckoutTitles() {
        return checkoutTitles;
    }


    public void removeFromCartByIndex(int index) {
        openCart();
        List<WebElement> removeButtons = driver.findElements(By.cssSelector(".cart_button"));
        if (index >= 1 && index <= removeButtons.size()) {
            removeButtons.get(index - 1).click();
            sleep(DELAY);
        }
    }

    public void storeCheckoutSummary() {
        paymentInfo  = driver.findElement(By.cssSelector("[data-test='payment-info-value']")).getText().trim();
        shippingInfo = driver.findElement(By.cssSelector("[data-test='shipping-info-value']")).getText().trim();
        itemTotal    = driver.findElement(By.cssSelector("[data-test='subtotal-label']")).getText().replace("Item total: ", "").trim();
        tax          = driver.findElement(By.cssSelector("[data-test='tax-label']")).getText().replace("Tax: ", "").trim();
        total        = driver.findElement(By.cssSelector("[data-test='total-label']")).getText().replace("Total: ", "").trim();
    }

    public String getPaymentInfo()   { return paymentInfo; }
    public String getShippingInfo()  { return shippingInfo; }
    public String getItemTotal()     { return itemTotal;    }
    public String getTax()           { return tax;          }
    public String getTotal()         { return total;        }

    /**
     * Verifies that the calculated cart sum plus tax matches the displayed total.
     */
    public boolean verifyTotal() {
        double cartSum = calculateCartSum();
        double displayedTax = Double.parseDouble(getTax().replace("$", ""));
        double displayedTotal = Double.parseDouble(getTotal().replace("$", ""));
        return Math.abs((cartSum + displayedTax) - displayedTotal) < 0.01;
    }

    private void sleep(int seconds) {
        try { Thread.sleep(seconds * 1000L); } catch (InterruptedException ignored) {}
    }

    public void quit() {
        if (driver != null) driver.quit();
    }

}