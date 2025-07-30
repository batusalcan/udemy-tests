import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test-No: 10
    Test ID: TC-BVA-S1
    Test Feature: Search Bar
    Test Description: Keyword length tested at boundary values [1, 100] – valid; 0 and 101 – invalid
    Test Technique: Boundary Value Analysis
    Tester: Batuhan Salcan
*/

public class TC_BVA_S1 {

    // Note that result are dynamically change due to discudemy web app behavior.
    //  4 test done in this class 3 of them is pass. Failed ->E1: lenght = 100 (VALID_KEYWORD_MAX)

    private static final String URL = "https://www.discudemy.com/search";

    // --- Valid Boundary Inputs ---
    private static final String VALID_KEYWORD_MIN = "j";                                  // E1: length = 1
    private static final String VALID_KEYWORD_MAX = "python".repeat(16) + "py";     // E1: length = 100

    // --- Invalid Inputs ---
    private static final String INVALID_KEYWORD_EMPTY = "";                               // U1: length = 0
    private static final String INVALID_KEYWORD_TOO_LONG = "a".repeat(101);         // U2: length = 101

    private DiscUdemyTestBot bot;

    @BeforeEach
    void setUp() {
        bot = new DiscUdemyTestBot(URL);
    }

    // E1 – Valid minimum length (1 character)
    @Test
    @DisplayName("E1a - Keyword of length 1 returns limited but valid results")
    public void testE1a_ValidKeywordMinLength() {
        bot.searchByKeyword(VALID_KEYWORD_MIN);
        List<String> titles = bot.getCourseTitles();

        assertFalse(titles.isEmpty(), "E1a: Expected at least 1 result for 1-character keyword.");

        boolean containsChar = titles.stream().anyMatch(t ->
                t.toLowerCase().contains(VALID_KEYWORD_MIN));
        assertTrue(containsChar, "E1a: No course titles contain the single-character keyword.");
    }

    // E1 – Valid maximum length (100 characters)
    @Test
    @DisplayName("E1b - Keyword of length 100 returns valid results")
    public void testE1b_ValidKeywordMaxLength() {
        bot.searchByKeyword(VALID_KEYWORD_MAX);
        List<String> titles = bot.getCourseTitles();

        assertFalse(titles.isEmpty(), "E1b: Expected results for 100-character keyword.");

        boolean relevant = titles.stream().anyMatch(t ->
                t.toLowerCase().contains("python"));
        assertTrue(relevant, "E1b: No course titles appear relevant to the keyword 'python'.");
    }

    // U1 – Invalid input (empty string)
    @Test
    @DisplayName("U1 - Empty keyword returns no results")
    public void testU1_EmptyKeyword() {
        bot.searchByKeyword(INVALID_KEYWORD_EMPTY);
        List<String> titles = bot.getCourseTitles();

       assertTrue(titles.isEmpty(), "U1: Expected no results for empty keyword.");
    }

    // U2 – Invalid input (length = 101)
    @Test
    @DisplayName("U2 - Keyword longer than 100 characters returns no results")
    public void testU2_TooLongKeyword() {
        bot.searchByKeyword(INVALID_KEYWORD_TOO_LONG);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(), "U2: Expected no results for keyword longer than 100 characters.");
    }
}
