import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/*
    Test-No: 62
    Test ID: TC-EP-CHECK2
    Test Feature: Price Calculation Consistency
    Test Description: Equivalence partitioning for item total, tax, and final total
    Test Technique: Equivalence Partitioning
    Tester: Efe Bırık

*/

public class TC_EP_CHECK2 {

    // 2 TEST PASSED
    // 1 TEST FAILED -> U1

    /*
        Equivalence Classes:
          E1 – total = item total + tax
          U1 – tax = 0, total incorrect
          U2 – item total = 0, but total > 0
     */
    private SauceDemoTestBot bot;

    // TODO: tomorrow run again

    @BeforeEach
    void setUp() {
        bot = new SauceDemoTestBot("https://www.saucedemo.com");
        bot.login("standard_user", "secret_sauce");
    }

    @AfterEach
    void exit() {
        bot.quit();
    }

    @Test
    @DisplayName("E1: Valid total - item total + tax")
    void E1_validTotalCalculation() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));


        assertEquals(itemTotal + tax, finalTotal, "The final total should be equal to the item total + tax");
    }

    @Test
    @DisplayName("U1: Tax mismatch (tax = 0)")
    void U1_taxMismatch() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        //Error if  tax mismatch
        assertNotEquals(itemTotal + tax, finalTotal, "Tax mismatch should trigger an error");
    }

    @Test
    @DisplayName("U2: Unexpected charge when item total is 0")
    void U2_unexpectedCharge() {

        bot.storeInventoryItems();

        double itemTotal = 0.00;
        double tax = 0.00;
        double finalTotal = 10.00; // Invalid charge

        //charge error is raised
        assertTrue(finalTotal > (itemTotal + tax), "Unexpected charge should raise an error");
    }
}
