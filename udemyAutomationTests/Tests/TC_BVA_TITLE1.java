import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
/*
    Test-No: 36 (new)
    Test ID: TC-BVA-TITLE1
    Test Feature: Category
    Test Description: Course title with  1-100 characters is accepted
    Test Technique: Boundary Value Analysis
    Tester: Demir Demirdöğen
*/
public class TC_BVA_TITLE1 {

    // ALL TEST IS PASSED.  4 PASS 0 FAIL

    private DiscUdemyTestBot bot;
    private static final String URL = "https://www.discudemy.com/search";

    // U1 – Invalid: Empty input
    private static final String TITLE_U1 = "";

    // E1 – Valid: Lower and upper bounds
    private static final String TITLE_E1_MIN = "J";  // 1 character
    private static final String TITLE_E1_MAX = "Java Programming Language Complete Beginner " +
                                                "to Advanced Full Stack Masterclass With Real Projects"; // 100 chars

    // U2 – Invalid: 101 characters
    private static final String TITLE_U2 = TITLE_E1_MAX + "X"; // 101 chars

    @BeforeEach
    void setup() {
        bot = new DiscUdemyTestBot(URL);
    }

    @AfterEach
    void exit() {
        bot.quit();
    }

    @Test
    @DisplayName("U1 - Search with empty title (0 chars) → Should return no results")
    void testTitleWithZeroChars() {
        bot.search(TITLE_U1);
        bot.storeData();
        List<String> results = bot.getCourseTitles();

        assertTrue(results.isEmpty(),
                "Expected: No results for empty search input. Found: " + results.size());
    }

    @Test
    @DisplayName("E1-Min - Search with 1 character → Should return results")
    void testTitleWithOneChar() {
        bot.search(TITLE_E1_MIN);
        bot.storeData();
        List<String> results = bot.getCourseTitles();

        assertFalse(results.isEmpty(),
                "Expected: Valid results for 1-character input, but got none.");
    }

    @Test
    @DisplayName("E1-Max - Search with 100-character title → Should be accepted")
    void testTitleWith100Chars() {
        bot.search("Java Programming Masterclass");  // partial match from the 100-char title
        bot.storeData();
        List<String> results = bot.getCourseTitles();

        boolean found = results.stream().anyMatch(t -> t.length() == 100);

        assertTrue(found,
                "Expected: A title with exactly 100 characters should exist in results.");
    }

    @Test
    @DisplayName("U2 - Search with 101 characters → Should return no results")
    void testTitleWith101Chars() {
        bot.search(TITLE_U2);
        bot.storeData();
        List<String> results = bot.getCourseTitles();

        assertTrue(results.isEmpty(),
                "Expected: No results for 101-character title. Found: " + results.size());
    }
}
