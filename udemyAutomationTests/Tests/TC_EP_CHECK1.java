import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/*
    Test-No: 61
    Test ID: TC-EP-CHECK1
    Test Feature: Cart vs Checkout Consistency
    Test Description: Equivalence classes for verifying cart and checkout match
    Test Technique: Equivalence Partitioning
    Tester: Yağmur Pazı

*/

public class TC_EP_CHECK1 {

    // All Test are passed 3/3
    /*
      Equivalence Classes:
          E1 – Cart and Checkout items match exactly
          U1 – Cart items differ from Checkout page
          U2 – Cart is empty, but checkout has items
     */

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
    @DisplayName("E1: Cart and Checkout items match exactly")
    void E1_cartCheckoutMatch() {

        // Got same item
        bot.storeInventoryItems();
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);


        bot.clickCheckout();
        bot.storeCheckoutItems();

        // Check -> Cart and Checkout must have the same items
        List<String> cartItems = bot.getCartTitles();
        List<String> checkoutItems = bot.getCheckoutTitles();
        assertEquals(cartItems, checkoutItems, "Cart and Checkout items should match exactly");
    }

    @Test
    @DisplayName("U1: Cart items differ from Checkout page")
    void U1_cartCheckoutMismatch() {
        // Cart and Checkout have different items
        bot.storeInventoryItems();
        bot.addToCartByIndex(1);

        //Try to add a different course in checkout
        bot.clickCheckout();
        bot.storeCheckoutItems();

        // Error message for inconsistent items should appear
        assertNotEquals(bot.getCartTitles(), bot.getCheckoutTitles(), "Cart and Checkout items must match");
    }

    @Test
    @DisplayName("U2: Cart is empty, but checkout has items")
    void U2_emptyCartCheckoutItems() {

        bot.clickCheckout();
        bot.storeCheckoutItems();


        assertTrue(bot.getCheckoutTitles().isEmpty(), "Checkout should not have any items if the cart is empty");
    }
}
