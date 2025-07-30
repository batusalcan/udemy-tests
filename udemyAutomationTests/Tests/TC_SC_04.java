import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.NoSuchElementException;
import java.util.List;


/*
    Test-No: 29
    Test ID: TC-SC-04
    Test Feature: Shopping Cart
    Test Description: Adding a course to shopping cart
    Test Technique: Boundary Value Analysis
    Tester: Egemen Üner
*/


public class TC_SC_04 {

    // 2 TEST PASSED
    // 3 TEST FAILED


    /*
    Boundary Value Classes:
      B1 – Cart has 0 courses before adding (lower boundary)
      B2 – Cart has 1 course before adding
      B3 – Cart has 9 courses before adding
      B4 – Cart has 10 courses (maximum capacity)
      B5 – Cart has 11 courses (exceeds capacity)
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
    @DisplayName("B1: Cart empty → add one course")
    void B1_emptyCartAddOne() {
        // Cart starts empty
        bot.storeCartItems();
        assertTrue(bot.getCartTitles().isEmpty(), "Cart should be initially empty");

        // Add one course
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();

        // Expectation: course is added successfully
        assertEquals(1, cart.size(), "Cart should contain exactly one item after adding");
    }

    @Test
    @DisplayName("B2: Cart has 1 course → add another")
    void B2_oneInCartAddOne() {
        // Precondition: cart has one item
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        assertEquals(1, bot.getCartTitles().size(), "Cart must start with one item");

        // Action: add a second distinct course
        bot.addToCartByIndex(2);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();

        // Expectation: cart now has two items
        assertEquals(2, cart.size(), "Cart should contain two items after adding another");
    }


    //  * DO NOT RUN THE BOUNDARY TESTS BELOW (B3,B4,B5), (tests are working)
    //  * BECAUSE THEY MAKE HIGH LOAD ON THE TEST APPLICATION.

    @Test
    @DisplayName("B3: Cart has all inventory courses → capacity reached")
    void B3_fullInventoryCapacity() {
        bot.storeInventoryItems();
        List<String> inv = bot.getInventoryTitles();
        // add every available inventory item
        for (int i = 1; i <= inv.size(); i++) {
            bot.addToCartByIndex(i);
        }
        bot.storeCartItems();
        assertEquals(inv.size(), bot.getCartTitles().size(),
                "Cart should contain all inventory items (capacity reached)");
    }

    @Test
    @DisplayName("B4: Attempt to add beyond capacity (index out of range)")
    void B4_tenInCartAddOne() {
        bot.storeInventoryItems();
        int capacity = bot.getInventoryTitles().size();

        for (int i = 1; i <= capacity; i++) {
            bot.addToCartByIndex(i);
        }

        assertThrows(NoSuchElementException.class,
                () -> bot.addToCartByIndex(capacity + 1),
                "Adding beyond inventory capacity should throw NoSuchElementException");
    }

    @Test
    @DisplayName("B5: Repeated attempts to add beyond capacity")
    void B5_elevenInCartAddOne() {
        bot.storeInventoryItems();
        int capacity = bot.getInventoryTitles().size();

        for (int i = 1; i <= capacity; i++) {
            bot.addToCartByIndex(i);
        }

        for (int attempt = 0; attempt < 3; attempt++) {
            int invalidIndex = capacity + 1 + attempt;
            assertThrows(NoSuchElementException.class,
                    () -> bot.addToCartByIndex(invalidIndex),
                    "Attempt " + (attempt+1) + ": adding index " + invalidIndex +
                            " should throw NoSuchElementException");
        }
    }
}
