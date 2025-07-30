import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/*
    Test-No: 63
    Test ID: TC-DT-CHECK3
    Test Feature: Checkout Validation
    Test Description: Validating combinations of cart items and total calculation accuracy
    Test Technique: Decision Table
    Tester: Efe Bırık

*/

public class TC_DT_CHECK3 {

    //  ONLY RULE-1 WILL PROCEED  PAYMENT
    // RULE-2,3,4,5,6,7,8 WILL STUCK AT SOME POINT

    //  3 TEST IS PASSED
    //  4 TEST IS FAILED --> 4 OF THEM IS NOT FEASIBLE SOMEHOW ( not sure )

    private SauceDemoTestBot bot;

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
    @DisplayName("Rule 1: Cart = Checkout, Item Total correct, Total = Item + Tax")
    void rule1_validCheckout() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        // Proceed to payment
        assertEquals(itemTotal + tax, finalTotal, "Total should equal item total + tax");
        assertDoesNotThrow(() -> bot.verifyTotal(), "Proceed to payment is valid");
    }

    @Test
    @DisplayName("Rule 2: Cart = Checkout, Item Total correct, Total != Item + Tax")
    void rule2_invalidTotalCalculation() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        assertNotEquals(itemTotal + tax, finalTotal, "Error: Calculation mismatch");
    }

    @Test
    @DisplayName("Rule 3: Cart != Checkout, Item Total correct, Total = Item + Tax")
    void rule3_checkoutMismatch() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        List<String> cartItems = bot.getCartTitles();
        List<String> checkoutItems = bot.getCheckoutTitles();


        assertNotEquals(cartItems, checkoutItems, "Cart and checkout items should match");
    }

    @Test
    @DisplayName("Rule 4: Cart = Checkout, Item Total incorrect, Total = Item + Tax")
    void rule4_invalidItemTotal() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        //Show Calculation Error
        assertNotEquals(itemTotal + tax, finalTotal, "Error: Item total mismatch");
    }

    @Test
    @DisplayName("Rule 5: Cart = Checkout, Item Total correct, Total = Item + Tax")
    void rule5_validTotalCalculation() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        // Proceed to payment
        assertEquals(itemTotal + tax, finalTotal, "Total should equal item total + tax");
        assertDoesNotThrow(() -> bot.verifyTotal(), "Proceed to payment is valid");
    }

    @Test
    @DisplayName("Rule 6: Cart != Checkout, Item Total incorrect, Total = Item + Tax")
    void rule6_checkoutMismatchWithIncorrectItemTotal() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        List<String> cartItems = bot.getCartTitles();
        List<String> checkoutItems = bot.getCheckoutTitles();

        // Show Calculation Error
        assertNotEquals(cartItems, checkoutItems, "Cart and checkout items should match");
    }

    @Test
    @DisplayName("Rule 7: Cart != Checkout, Item Total correct, Total != Item + Tax")
    void rule7_invalidTotalCalculationWithMismatch() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        // Show Calculation Error
        assertNotEquals(itemTotal + tax, finalTotal, "Error: Calculation mismatch");
    }

    @Test
    @DisplayName("Rule 8: Cart != Checkout, Item Total incorrect, Total != Item + Tax")
    void rule8_invalidTotalCalculationWithMismatchAndItems() {

        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.clickCheckout();
        bot.fillCheckoutInformation("Efe", "Test", "12345");
        bot.storeCheckoutSummary();


        double itemTotal = Double.parseDouble(bot.getItemTotal().replace("$", ""));
        double tax = Double.parseDouble(bot.getTax().replace("$", ""));
        double finalTotal = Double.parseDouble(bot.getTotal().replace("$", ""));

        // Show Calculation Error
        assertNotEquals(itemTotal + tax, finalTotal, "Error: Invalid cart and checkout values");
    }
}
