package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.time.Duration;

/**
 * Selenium TestNG Automated Test Suite for DevOps Login Web Application
 * Primary Reference: College Experiment 6 - Selenium Java Project Integration with GitHub Actions
 */
public class LocalLoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Configure ChromeOptions for stable headless and CI/CD execution
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        // Initialize ChromeDriver with configured options
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Locate login.html either from project directory or classpath
        File htmlFile = new File("src/main/resources/webpage/login.html");
        String fileUrl;
        if (htmlFile.exists()) {
            fileUrl = htmlFile.toURI().toString();
        } else {
            URL resourceUrl = getClass().getClassLoader().getResource("webpage/login.html");
            if (resourceUrl != null) {
                fileUrl = resourceUrl.toExternalForm();
            } else {
                throw new IllegalStateException("Error: login.html file could not be located.");
            }
        }

        // Navigate to the local login webpage
        driver.get(fileUrl);
    }

    /**
     * TEST 1: validLoginTest
     * Enters valid credentials (student / 12345), submits the form,
     * and verifies that the success message is displayed.
     */
    @Test(priority = 1)
    public void validLoginTest() {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("loginButton"));

        // Enter valid credentials
        usernameField.clear();
        usernameField.sendKeys("student");
        passwordField.clear();
        passwordField.sendKeys("12345");

        // Click login button
        loginBtn.click();

        // Verify login success message
        WebElement messageElement = driver.findElement(By.id("message"));
        String actualMessage = messageElement.getText().trim();
        String expectedMessage = "Welcome to DevOps! Login Successful";

        Assert.assertEquals(actualMessage, expectedMessage, "Success message mismatch!");
    }

    /**
     * TEST 2: invalidLoginTest
     * Enters invalid credentials, submits the form,
     * and verifies that the error message is displayed.
     */
    @Test(priority = 2)
    public void invalidLoginTest() {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("loginButton"));

        // Enter invalid credentials
        usernameField.clear();
        usernameField.sendKeys("invalidUser");
        passwordField.clear();
        passwordField.sendKeys("wrongPassword");

        // Click login button
        loginBtn.click();

        // Verify invalid login message
        WebElement messageElement = driver.findElement(By.id("message"));
        String actualMessage = messageElement.getText().trim();
        String expectedMessage = "Invalid Username or Password";

        Assert.assertEquals(actualMessage, expectedMessage, "Invalid login error message mismatch!");
    }

    /**
     * TEST 3: checkBackgroundColor
     * Locates the loginBox element and verifies its light-blue background color.
     */
    @Test(priority = 3)
    public void checkBackgroundColor() {
        WebElement loginBox = driver.findElement(By.id("loginBox"));
        String actualBgColor = loginBox.getCssValue("background-color");

        // CSS lightblue translates to rgb(173, 216, 230) or rgba(173, 216, 230, 1) in browser rendering
        boolean isLightBlue = actualBgColor.contains("173, 216, 230") || 
                              actualBgColor.equalsIgnoreCase("lightblue") ||
                              actualBgColor.equalsIgnoreCase("#add8e6");

        Assert.assertTrue(isLightBlue, 
            "Expected loginBox background to be light-blue (rgb(173, 216, 230)), but found: " + actualBgColor);
    }

    @AfterMethod
    public void tearDown() {
        // Safely quit driver and terminate browser session
        if (driver != null) {
            driver.quit();
        }
    }
}
