# SE 2226 Project – Udemy Web Application Test Plan


## Team Members:

- Demir Demirdöğen -23070006036
- Yağmur Pazı - 23070006066
- Batuhan Salcan - 22070006040
- Egemen Üner - 23070006051
- Efe Bırık - 22070006075

---

## Project Overview

This project is about testing a learning website using simple and clear testing methods. The goal is to check if the website works well for users, even when they do unexpected things.
We do not look at the code inside the website. Instead, we test it from the outside — like a real user would. We try different actions such as searching, choosing courses, and going to checkout, and we check if everything works correctly.
The tests are written using tools like Selenium and JUnit. These tools help us run the tests automatically.
This project shows how testing can help improve the quality of a website by making sure it works properly and gives a good user experience.



---

## Test Bots Summary

| Bot Class              | Target Platform             | Description                                                                 |
|------------------------|-----------------------------|-----------------------------------------------------------------------------|
| `DiscUdemyTestBot`     | [discudemy.com](https://www.discudemy.com) | Handles search tests, category navigation, and course title validation.     |
| `UdemyTestBot`         | [udemy.com](https://www.udemy.com)         | Used for search testing (not stable due to Cloudflare bot protection).     |
| `SauceDemoTestBot`     | [saucedemo.com](https://www.saucedemo.com) | Simulates cart, checkout, price calculations, and full purchase flows.     |

> Note: Due to bot protection on Udemy, **DiscUdemyTestBot** is the preferred bot for keyword testing.  
>**SauceDemoTestBot** is used as a reliable e-commerce simulation environment.

--- 

## Test Cases Overview

**Total Automated JUnit Bot Test Classes:** 14
All test cases listed above are implemented using JUnit 5 and execute through automated Selenium-based test bots.

| Test ID         | Test Technique            | Description                                                                 |
|-----------------|---------------------------|-----------------------------------------------------------------------------|
| TC-EP-S1        | Equivalence Partitioning  | Valid/invalid keywords checked for relevance in course search results.     |
| TC-BVA-S1       | Boundary Value Analysis   | Verifies keyword length: 0, 1, 100, 101 characters.                        |
| TC-BVA-TITLE1   | Boundary Value Analysis   | Tests course title length boundaries: 0 (invalid), 1–100 (valid), 101.     |
| TC-EP-TITLE1    | Equivalence Partitioning  | Ensures course titles contain only valid characters (letters/spaces).      |
| TC-EP-TITLE2    | Equivalence Partitioning  | Searches with forbidden characters (!@#$%*) must return no results.        |
| TC-UC-CAT1      | Use Case Testing          | Navigates through multiple category buttons successfully.                  |
| TC-SC-01        | Use Case Testing          | Adds item to cart, tests login-required and duplicate adds.                |
| TC-SC-02        | Use Case Testing          | Removes courses under various scenarios: normal, guest, multi-remove.      |
| TC-SC-03        | Equivalence Partitioning  | Tests adding valid, duplicate, invalid, and guest-course scenarios.        |
| TC-SC-04        | Boundary Value Analysis   | Validates cart behavior at limits: empty → full → overflow.                |
| TC-EP-CHECK1    | Equivalence Partitioning  | Verifies cart and checkout contents match (valid/invalid partitions).      |
| TC-EP-CHECK2    | Equivalence Partitioning  | Ensures total = item total + tax. Checks unexpected total or 0-tax cases.  |
| TC-DT-CHECK3    | Decision Table Testing    | Tests 8 rules combining cart/checkout mismatch and total errors.           |
| TC-UC-F1        | Use Case Testing          | Simulates full purchase flow: login → select → cart → checkout.            |

---

### Development Environment
- **Operating System:** Windows 11
- **IDE:** IntelliJ IDEA Ultimate
- **JDK Version:** JDK 21
- **Programming Language:** Java

### Testing Tools & Libraries
- **JUnit 5:** For unit test structure and parameterized testing
- **Selenium WebDriver:** For automating browser actions and UI interaction
- **ChromeDriver (Win64):** To run Selenium tests in the Chrome browser

### Browsers & Drivers
- **Google Chrome:** Used for executing all Selenium-based test scenarios
- **ChromeDriver:** Compatible with the installed browser version and OS

---

## Notes

- All tests are black-box and do not interact with or modify any server-side code.
- No data is persisted or submitted to Udemy. All executions are automated through visible front-end interaction.
- Test failures may occur intermittently due to network speed, bot detection, or dynamic course listing changes.



>This project is developed for academic purposes only, as part of the SE 2226 course. No source code from Udemy is accessed or modified. All tests are performed externally through UI automation and black-box analysis.