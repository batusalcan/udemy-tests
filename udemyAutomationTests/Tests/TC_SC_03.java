import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.NoSuchElementException;

import java.util.List;

/*
    Test-No: 28
    Test ID: TC-SC-03
    Test Feature: Shopping Cart
    Test Description: Adding a course to shopping cart
    Test Technique: Equivalence Partitioning
    Tester: Batuhan Salcan
 */
public class TC_SC_03 {

    /*
        Equivalence Partitioning Classes for TC-SC-03:
        E1 – Valid paid course, not purchased, not in cart
        E2 – Another valid paid course (different price/instructor)
        U1 – Course already in cart
        U2 – Course already purchased  ----> NOT TESTABLE IN THIS WEB APP
        U3 – Invalid course (deleted or non-existent)
        U4 – User not logged in
        U5 – Course is free (should not go to cart) ----> NOT TESTABLE IN THIS WEB APP
        U6 – Course unpublished/inactive after added ----> NOT TESTABLE IN THIS WEB APP

    */


    // 3 TEST PASSED
    // 2 TEST FAILED
    // 3 TEST CASES IS NOT TESTABLE IN THIS APP

    private SauceDemoTestBot bot;

    @BeforeEach
    void setUp() {
        bot = new SauceDemoTestBot("https://www.saucedemo.com");
    }

    @AfterEach
    void exit() {
        bot.quit();
    }

    @Test
    @DisplayName("E1: Add a valid paid course (not purchased, not in cart)")
    void E1_validPaidCourse() {
        bot.login("standard_user", "secret_sauce");
        bot.storeInventoryItems();
        List<String> inv = bot.getInventoryTitles();
        assertFalse(inv.isEmpty(), "Inventory must not be empty");

        // pick first item
        String course = inv.get(0);
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();

        assertEquals(1, cart.size(), "Cart should contain exactly one item");
        assertEquals(course, cart.get(0), "That item should be the one added");
    }

    @Test
    @DisplayName("E2: Add another valid paid course (different instructor/price)")
    void E2_anotherValidPaidCourse() {
        bot.login("standard_user", "secret_sauce");
        bot.storeInventoryItems();
        List<String> inv = bot.getInventoryTitles();
        assertTrue(inv.size() >= 2, "Need at least two products");

        // pick second item
        String course = inv.get(1);
        bot.addToCartByIndex(2);
        bot.storeCartItems();
        List<String> cart = bot.getCartTitles();

        assertTrue(cart.contains(course), "Cart should contain the second course");
    }

    @Test
    @DisplayName("U1: Attempt to add a course already in cart")
    void U1_courseAlreadyInCart() {
        bot.login("standard_user", "secret_sauce");
        bot.storeInventoryItems();
        String course = bot.getInventoryTitles().get(0);

        bot.addToCartByIndex(1);
        bot.storeCartItems();
        assertEquals(1, bot.getCartTitles().size(), "Cart has one item");

        // try to add the same course again
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cartAfter = bot.getCartTitles();

        // expecting no duplicate
        assertEquals(1, cartAfter.size(), "Cart should still contain only one instance");
        assertEquals(course, cartAfter.get(0), "That instance should match the added course");
    }


    @Test
    @DisplayName("U3: Attempt to add an invalid (non-existent) course")
    void U3_invalidCourse() {
        bot.login("standard_user", "secret_sauce");
        int invalidIndex = bot.getInventoryTitles().size() + 1;
        assertThrows(NoSuchElementException.class,
                () -> bot.addToCartByIndex(invalidIndex),
                "Adding non-existent course should throw NoSuchElementException");
    }

    @Test
    @DisplayName("U4: Not-logged-in user tries to add a course")
    void U4_notLoggedIn() {
        assertThrows(NoSuchElementException.class,
                () -> bot.addToCartByIndex(1),
                "Guest user should not be able to add to cart");
    }

}
