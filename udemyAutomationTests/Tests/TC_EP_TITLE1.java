import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

/*
    Test-No: 34 (new)
    Test ID: TC-EP-TITLE1
    Test Feature: Course Title
    Test Description: Course title with only letters and spaces is accepted
    Test Technique: Equivalence Partitioning
    Tester: Egemen Üner
*/
public class TC_EP_TITLE1 {

    private DiscUdemyTestBot bot;
    private static final String URL = "https://www.discudemy.com/search";

    // 8/10
    // 8  TESTS IS PASSED
    // 2  TEST IS FAILED (don't know why result seems true to me)


    // E
    private static final List<String> expectedTitles = Arrays.asList(
            // Expected Inputs
            "Java Programming for Beginners",  // E1
            "Learn Python 3.10 from Scratch",  // E2
            "HTML & CSS Basics",               // E3
            "C# Fundamentals – .NET Core",     // E4
            "Machine Learning Bootcamp"        // E5
    );

    // U
    private static final List<String> unexpectedTitles = Arrays.asList(
            // Unexpected Inputs
            "Java! Programming",       // U1  Contains '!'
            "C++ #1 Guide",            // U2  Contains '+', '#'
            "@Python for Data*",       // U3  Contains '@', '*'
            "SQL & Databases$",        // U4  Contains '$'
            "🚀 Fullstack Dev Course"  // U5  Contains emoji
    );

    @BeforeEach
    void setup() {
        bot = new DiscUdemyTestBot(URL);
    }

    @Test
    @DisplayName("TC-EP-TITLE1 - Course title with only letters and spaces is accepted")
    void testOnlyLettersAndSpacesAreAccepted() {
        String keyword = "java";

        bot.search(keyword);
        bot.storeData();
        List<String> resultTitles = bot.getCourseTitles();

        String allowedPattern = "[a-zA-Z0-9\\s:\\-.,()'’]+";

        List<String> invalidTitles = resultTitles.stream()
                .filter(title -> !title.matches(allowedPattern))
                .toList();


        assertTrue(invalidTitles.isEmpty(),
                "Unexpected character(s) found in course titles.\nInvalid Titles:\n" + String.join("\n", invalidTitles));

        bot.quit();
    }

}
