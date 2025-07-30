import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


/*
    Test-No: 42
    Test ID: TC-UC-F1
    Test Feature: Purchase Flow
    Test Description: login -> homepage -> category -> sub-category -> select course -> add to cart -> checkout
    Test Technique: Use Case Testing
    Test Data: Efe Bırık
    Tester: Efe Bırık / Demir Demirdöğen
*/


// NOTE: This test case covers the entire purchase flow from login through checkout.
public class TC_UC_PF1 {
    //PF ---> Payment Flow

    // PASSED WIWTHOUT ANY ISSUE OR PROBLEM
    // 1 HUGE TEST IS PASSED


    private SauceDemoTestBot bot;

    @BeforeEach
    void setUp() {
        bot = new SauceDemoTestBot("https://www.saucedemo.com");
    }

    @AfterEach
    void  exit() {
        bot.quit();
    }

    // login -> homepage -> category -> sub-category -> select course -> add to cart -> checkout
    @Test
    @DisplayName("TC-UC-F1: Full purchase flow")
    void fullPurchaseFlow() {
        // 1. Login
        bot.login("standard_user", "secret_sauce");

        // 2. Homepage → store available inventory
        bot.storeInventoryItems();
        List<String> inventory = bot.getInventoryTitles();
        assertFalse(inventory.isEmpty(), "Homepage should show available courses");

        // 3. Select first course
        String selectedCourse = inventory.get(0);

        // 4. Add to cart
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();
        assertEquals(1, cart.size(), "Cart should contain exactly one course");
        assertEquals(selectedCourse, cart.get(0), "Cart must contain the selected course");

        // 5. Proceed to checkout
        bot.clickCheckout();

        // 6. Fill in checkout information
        bot.fillCheckoutInformation("Efe", "Tester", "12345");

        // 7. Verify checkout overview shows the same course
        bot.storeCheckoutItems();
        List<String> checkout = bot.getCheckoutTitles();
        assertEquals(cart, checkout, "Checkout overview must list the same course as in cart");

        // 8. Capture and verify payment & total summary
        bot.storeCheckoutSummary();
        assertNotNull(bot.getPaymentInfo(), "Payment info must be displayed");
        assertNotNull(bot.getShippingInfo(), "Shipping info must be displayed");
        assertTrue(bot.getItemTotal().startsWith("$"), "Item total must be shown");
        assertTrue(bot.getTax().startsWith("$"), "Tax must be shown");
        assertTrue(bot.getTotal().startsWith("$"), "Total must be shown");

        // 9. Final validate:
        // Focus ->  total = item total + tax
        assertTrue(bot.verifyTotal(), "Displayed total should equal item total plus tax");
    }
}
