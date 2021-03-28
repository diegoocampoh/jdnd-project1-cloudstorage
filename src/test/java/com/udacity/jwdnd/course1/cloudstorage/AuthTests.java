package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@Test
	public void testUnauthorizedAccess() {
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement login = wait.until(webDriver -> webDriver.findElement(By.id("loginForm")));
		Assertions.assertTrue(login.isDisplayed());
	}


	private void signup(String username, String password){
		driver.get("http://localhost:" + port + "/signup");
		signupPage = new SignupPage(driver);
		signupPage.typeSignupDetails(
				"testUser",
				"testUser",
				username,
				password);
		signupPage.clickSignup();
	}

	private void login(String username, String password){
		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.typeLoginDetails(
				username,
				password
		);
		loginPage.clickLogin();
	}


	@Test
	public void testSignupAndLoginAndLogut() {

		String username = "username";
		String password = "password";
		signup(username, password);
		Assertions.assertTrue(signupPage.getSuccessMessage().startsWith("You successfully signed up!"));

		login(username, password);

		driver.get("http://localhost:" + port + "/home");
		homePage = new HomePage(driver);
		homePage.logout();

		//after logging out and trying to hit Home, we should be redirected to login.

		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement login = wait.until(webDriver -> webDriver.findElement(By.id("loginForm")));
		Assertions.assertTrue(login.isDisplayed());
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
	}
}
