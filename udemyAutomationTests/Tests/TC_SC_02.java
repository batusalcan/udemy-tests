
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.NoSuchElementException;
import java.util.List;

/*
    Test-No: 27
    Test ID: TC-SC-02
    Test Feature: Shopping Cart
    Test Description: Remove a course from the shopping cart
    Test Technique: Use Case Testing
    Tester: Efe Bırık
*/


public class TC_SC_02 {

    // 4/4 %100
    // 4 TEST PASSED
    // 0 FAIL

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
    @DisplayName("SC1: Normal remove action")
    void SC1_normalRemove() {

        bot.login("standard_user", "secret_sauce");
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        List<String> cartBefore = bot.getCartTitles();
        assertEquals(1, cartBefore.size(), "Cart should initially contain one item");


        bot.removeFromCartByIndex(1);
        bot.storeCartItems();
        List<String> cartAfter = bot.getCartTitles();


        assertTrue(cartAfter.isEmpty(), "Cart should be empty after removal");
    }

    @Test
    @DisplayName("SC2: Multiple items removed")
    void SC2_multipleRemove() {

        bot.login("standard_user", "secret_sauce");
        bot.addToCartByIndex(1);
        bot.addToCartByIndex(2);
        bot.storeCartItems();
        List<String> cartBefore = bot.getCartTitles();
        assertEquals(2, cartBefore.size(), "Cart should initially contain two items");

        // When: remove both items
        bot.removeFromCartByIndex(1);
        bot.removeFromCartByIndex(1); // after first removal, second item shifts to index 1

        bot.storeCartItems();
        List<String> cartAfter = bot.getCartTitles();

        // Then: all selected courses are removed
        assertTrue(cartAfter.isEmpty(), "Cart should be empty after removing all items");
    }

    @Test
    @DisplayName("SC3: Course already removed")
    void SC3_removeAlreadyRemoved() {
        // Given: a logged-in user with one item removed already
        bot.login("standard_user", "secret_sauce");
        bot.addToCartByIndex(1);
        bot.storeCartItems();
        assertEquals(1, bot.getCartTitles().size(), "Cart must contain one item");
        bot.removeFromCartByIndex(1);
        bot.storeCartItems();
        assertTrue(bot.getCartTitles().isEmpty(), "Cart should now be empty");

        // attempt to remove again should leave cart empty
        bot.removeFromCartByIndex(1);
        bot.storeCartItems();
        assertTrue(bot.getCartTitles().isEmpty(), "Cart remains empty when removing non-existent item");
    }

    @Test
    @DisplayName("SC4: Guest user tries to remove")
    void SC4_guestRemove() {
        // Try to remove without login should redirect to login page
        assertThrows(NoSuchElementException.class, () -> {
            bot.removeFromCartByIndex(1);
        }, "Guest user should be redirected or error when trying to remove");
    }
}
