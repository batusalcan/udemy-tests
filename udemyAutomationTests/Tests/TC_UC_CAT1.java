import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test-No: 52 (new)
    Test ID: TC-UC-CAT1
    Test Feature: Category
    Test Description: User navigates between categories
    Test Technique: Use Case Testing
    Tester: Demir Demirdöğen / Yağmur Pazı
*/
public class TC_UC_CAT1 {      // Category Navigation Test

    // 6 test is passed
    // Repeated Test

    private DiscUdemyTestBot bot;
    private static final String CATEGORY_URL = "https://www.discudemy.com/category";

    @BeforeEach
    void setUp() {
        bot = new DiscUdemyTestBot(CATEGORY_URL);
    }

    // Provides category names to test
    private static Stream<String> categoryProvider() {
        return Stream.of(
                "python",
                "java",
                "javascript",
                "csharp",
                "html",
                "css"
        );
    }


    // Test passes if the current URL contains the expected category path after clicking the category button.
    // Example: Clicking "python" should redirect to ".../category/python"
    @ParameterizedTest
    @MethodSource("categoryProvider")
    @DisplayName("TC-UC-CAT1 - User can navigate to multiple category buttons")
    public void testCategoryNavigation(String categoryName) {
        bot.connect();
        bot.clickCategoryByName(categoryName);

        String expectedUrlPart = "/category/" + categoryName;
        String currentUrl = bot.getDriver().getCurrentUrl();

        assertTrue(currentUrl.contains(expectedUrlPart),
                "Failed to navigate to category: '" + categoryName + "'. Current URL: " + currentUrl);

        bot.getDriver().navigate().back();
        bot.waitSeconds(1);
        bot.quit();
    }
}
