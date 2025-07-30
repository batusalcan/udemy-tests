
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*
    Test-No: 18 (new Test-No: 9)
    Test ID: TC-EP-S1
    Test Feature: Search Bar
    Test Description: Valid keywords return relevant courses (result course title must include search keyword)
    Tester: Demir Demirdöğen
 */

public class TC_EP_S1 {

    // 7 TEST (ALL) PASSED

    private static final String URL = "https://www.discudemy.com/search";

    private static final String VALID_KEYWORD_1 = "python";                 // E1
    private static final String VALID_KEYWORD_2 = "web development";        // E2

    private static final String INVALID_KEYWORD_EMPTY = "";                              // U1
    private static final String INVALID_KEYWORD_SPECIAL = "@#$";                         // U2
    private static final String INVALID_KEYWORD_SHORT = "x";                             // U3
    private static final String INVALID_KEYWORD_LONG = "a".repeat(50);             // U4
    private static final String INVALID_KEYWORD_SPAM = "buy now click here free course"; // U5

    private DiscUdemyTestBot bot;

    @BeforeEach
    void setUp() {
        bot = new DiscUdemyTestBot(URL);
    }

    // E1: Valid common keyword
    // Test fails if any course title does not contain the expected keyword.
    @Test
    @DisplayName("E1 - Search with VALID_KEYWORD_1 should return related courses")
    public void testValidKeyword1() {

        bot.searchByKeyword(VALID_KEYWORD_1);
        List<String> titles = bot.getCourseTitles();

        for (String title : titles) {
            assertTrue(title.toLowerCase().contains(VALID_KEYWORD_1),
                    "Course title does not contain VALID_KEYWORD_1: " + title);
        }
    }

    // E2: Valid multi-word keyword
    // Test fails if course title does not contain at least one part of the multi-word keyword.
    @Test
    @DisplayName("E2 - Search with VALID_KEYWORD_2 should return related courses")
    public void testValidKeyword2() {

        bot.searchByKeyword(VALID_KEYWORD_2);
        List<String> titles = bot.getCourseTitles();

        for (String title : titles) {
            boolean containsAny = VALID_KEYWORD_2.split(" ")[0].equalsIgnoreCase("web") ||
                    VALID_KEYWORD_2.split(" ")[1].equalsIgnoreCase("development");

            assertTrue(title.toLowerCase().contains("web") || title.toLowerCase().contains("development"),
                    "Course title does not relate to VALID_KEYWORD_2: " + title);
        }
    }

    // U1: Empty input
    // Test fails if results are returned for an empty keyword input.
    @Test
    @DisplayName("U1 - Search with INVALID_KEYWORD_EMPTY should return no results")
    public void testInvalidKeywordEmpty() {

        bot.searchByKeyword(INVALID_KEYWORD_EMPTY);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(), "Expected no results for INVALID_KEYWORD_EMPTY.");
    }

    // U2: Special characters
    // Test fails if results are returned for meaningless special characters.
    @Test
    @DisplayName("U2 - Search with INVALID_KEYWORD_SPECIAL should return no results")
    public void testInvalidKeywordSpecial() {

        bot.searchByKeyword(INVALID_KEYWORD_SPECIAL);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(), "Expected no results for INVALID_KEYWORD_SPECIAL.");
    }

    // U3: Too short input
    // Test fails if too many results are returned for a very short keyword.
    @Test
    @DisplayName("U3 - Search with INVALID_KEYWORD_SHORT should return few or no results")
    public void testInvalidKeywordShort() {

        bot.searchByKeyword(INVALID_KEYWORD_SHORT);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.size() <= 2, "Expected very few or no results for INVALID_KEYWORD_SHORT.");
    }

    // U4: Excessively long string
    // Test fails if results are returned for an overly long and unrealistic keyword.
    @Test
    @DisplayName("U4 - Search with INVALID_KEYWORD_LONG should return no results")
    public void testInvalidKeywordLong() {

        bot.searchByKeyword(INVALID_KEYWORD_LONG);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(), "Expected no results for INVALID_KEYWORD_LONG.");
    }

    // U5: Spammy input
    // Test fails if spammy or irrelevant input produces valid-looking results.
    @Test
    @DisplayName("U5 - Search with INVALID_KEYWORD_SPAM should return no results")
    public void testInvalidKeywordSpam() {

        bot.searchByKeyword(INVALID_KEYWORD_SPAM);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(), "Expected no results for INVALID_KEYWORD_SPAM.");
    }
}
