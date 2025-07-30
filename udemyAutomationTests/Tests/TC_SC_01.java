import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

/*
    Test-No: 26
    Test ID: TC-SC-01
    Test Feature: Shopping Cart
    Test Description: Adding a course to the shopping cart
    Test Technique: Use Case Testing
    Tester: Yağmur Pazı
*/
public class TC_SC_01 {

    // SC-4 AND SC-5 IS NOT FEASIBLE TO TEST
    // SC-1 IS PASSED
    // SC-2 & SC-3 FAILED

    // 2/3 TEST ARE PASSED & 2 TEST IS NOT FEASIBLE
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
    @DisplayName("SC1: Successful add to cart")
    void SC1_successfulAddToCart() {

        bot.login("standard_user", "secret_sauce");
        bot.storeInventoryItems();
        List<String> inventory = bot.getInventoryTitles();
        assertFalse(inventory.isEmpty(), "Inventory must not be empty");


        String expected = inventory.get(0);
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();


        assertEquals(1, cart.size(), "Cart should contain exactly one item");
        assertEquals(expected, cart.get(0), "Cart item should match the one added");
    }

    @Test
    @DisplayName("SC2: Add to cart without login should prompt login")
    void SC2_addWithoutLogin() {

        assertThrows(NoSuchElementException.class, () -> {

            bot.addToCartByIndex(1);
        }, "Attempting to add without login should fail with NoSuchElementException");
    }

    @Test
    @DisplayName("SC3: Adding same item twice should not duplicate in cart")
    void SC3_duplicateAddShowsAlready() {
        bot.login("standard_user", "secret_sauce");
        bot.storeInventoryItems();
        String title = bot.getInventoryTitles().get(0);


        bot.addToCartByIndex(1);

        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();


        assertEquals(1, cart.size(), "Cart should still contain only one instance");
        assertEquals(title, cart.get(0), "That instance should match the added item");
    }


    // SC-4 Cant testable
    // SC-5 Cant testable


}
