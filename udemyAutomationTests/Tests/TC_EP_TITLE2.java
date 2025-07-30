import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/*
    Test-No: 35 (new)
    Test ID: TC_EP_TITLE2
    Test Feature: Course Title
    Test Description: Course title containing forbidden characters (!@#$%) is rejected
    Test Technique: Equivalence Partitioning
    Tester: Demir Demirdöğen
*/

public class TC_EP_TITLE2 {

    // ALL  6 test cases pass
    // This is repeated test so we run test-1 for all 6 forbiddenSearchKeywords

    private DiscUdemyTestBot bot;
    private static final String SEARCH_URL = "https://www.discudemy.com/search";
    private static final String CATEGORY_URL = "https://www.discudemy.com/category";
    private static final char[] FORBIDDEN_CHARS = { '!', '@', '#', '$', '%', '*' };

    @BeforeEach
    void setUp() {
        bot = null;
    }

    //Forbidden input keywords (invalid equivalence class)
    private static Stream<String> forbiddenSearchKeywords() {
        return Stream.of("!java", "@python", "#C", "$ML", "%sql", "*bootstrap");
    }

    //Valid categories to check for clean course titles (valid equivalence class)
    private static Stream<String> categoryListForValidation() {
        return Stream.of("python", "java");
    }

    // Unexpected (U) -> Forbidden search keywords should return no results
    // Test passes if no results are returned for forbidden inputs
    @ParameterizedTest
    @MethodSource("forbiddenSearchKeywords")
    @DisplayName("TC-EP-TITLE2-1 - Forbidden character search input returns no results")
    public void testForbiddenKeywordSearch(String keyword) {
        bot = new DiscUdemyTestBot(SEARCH_URL);
        bot.search(keyword);
        List<String> titles = bot.getCourseTitles();

        assertTrue(titles.isEmpty(),
                "Expected no results for forbidden keyword: '" + keyword + "', but got " + titles.size());

        bot.quit();
    }

    // Expected  (E)-> EP Positive Case: Valid category course titles must be clean
    // Test passes if course title does not contain any forbidden characters
    @ParameterizedTest
    @MethodSource("categoryListForValidation")
    @DisplayName("TC-EP-TITLE2-2 - Course titles in categories do not contain forbidden characters")
    public void testCategoryCourseTitlesAreClean(String categoryName) {
        bot = new DiscUdemyTestBot(CATEGORY_URL);
        bot.connect();
        bot.clickCategoryByName(categoryName);
        List<String> titles = bot.getCourseTitles();

        for (String title : titles) {
            for (char forbidden : FORBIDDEN_CHARS) {
                assertFalse(title.contains(String.valueOf(forbidden)),
                        "Course title contains forbidden character '" + forbidden + "' → " + title);
            }
        }

        bot.getDriver().navigate().back();
        bot.waitSeconds(1);
        bot.quit();
    }
}
