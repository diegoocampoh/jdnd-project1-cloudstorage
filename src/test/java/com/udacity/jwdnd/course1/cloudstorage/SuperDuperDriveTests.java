package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuperDuperDriveTests {
    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    private static WebDriver driver;
    private static boolean userSignupDone = false;
    private static String existingUsername = "user";
    private static String existingPassword = "Tr$81m8TJ3iJ6";

    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;
    private NotePage notePage;
    private CredentialPage credentialPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

    }

    @BeforeEach
    public void beforeEach() {
        if(!userSignupDone){
            userService.createUser(new User(
                    null,
                    existingUsername,
                    null,
                    existingPassword,
                    "userForTests",
                    "Selenium"

            ));

            userService.createUser(new User(
                    null	,
                    "userWithNote",
                    null,
                    "userWithNote",
                    "userForTests",
                    "Selenium"

            ));

            noteService.addNote(
                    new NoteForm(
                            null,
                            "userWithNote",
                            "existing note",
                            "existing ntoe description"
                    )
            );

            userService.createUser(new User(
                    null	,
                    "userWithCredential",
                    null,
                    "userWithCredential",
                    "userWithCredential",
                    "Selenium"

            ));

            credentialService.addCredential(
                    new CredentialForm(
                            null,
                            "credential url",
                            "userWithCredential",
                            "cred-username",
                            "cred-password"
                    )
            );

            userSignupDone = true;
        }
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

    @Test
    public void testUnauthorizedAccess() {
        driver.get("http://localhost:" + this.port + "/home");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        WebElement login = wait.until(webDriver -> webDriver.findElement(By.id("loginForm")));
        Assertions.assertTrue(login.isDisplayed());
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
    public void createNote(){
        login(existingUsername, existingPassword);

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        notePage = new NotePage(driver);

        String noteTitle = "note title";
        String noteDesc = "note desc";

        homePage.switchToNotes();

        notePage.addNote(noteTitle, noteDesc);

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);

        homePage.switchToNotes();

        Note note = notePage.getFirstNote();

        Assertions.assertEquals(noteTitle, note.getNotetitle());
        Assertions.assertEquals(noteDesc, note.getNotedescription());

        homePage.logout();
    }

    @Test
    public void editNote(){
        login("userWithNote", "userWithNote");

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        notePage = new NotePage(driver);

        String noteTitle = "updated title";
        String noteDesc = "updated desc";

        homePage.switchToNotes();

        notePage.editNote(noteTitle, noteDesc);

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);

        homePage.switchToNotes();

        Note note = notePage.getFirstNote();

        Assertions.assertEquals(noteTitle, note.getNotetitle());
        Assertions.assertEquals(noteDesc, note.getNotedescription());

        homePage.logout();
    }

    @Test
    public void deleteNote(){
        login("userWithNote", "userWithNote");

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        notePage = new NotePage(driver);
        homePage.switchToNotes();
        notePage.delete();
        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        homePage.switchToNotes();

        Assertions.assertEquals(0, driver.findElements(By.className("noteTitle")).size());

        homePage.logout();
    }

    @Test
    public void createCredential(){
        login(existingUsername, existingPassword);

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        credentialPage = new CredentialPage(driver);

        String credentialUrl = "credential URL";
        String credentialUsername = "credential username";
        String credentialPassword = "credential password";

        homePage.switchToCredentials();

        credentialPage.addCredential(
                credentialUrl,
                credentialUsername,
                credentialPassword
        );

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);

        homePage.switchToCredentials();

        Credential credential = credentialPage.getFirstCredential();

        Assertions.assertEquals(credentialUrl, credential.getUrl());
        Assertions.assertEquals(credentialUsername, credential.getUsername());
        Assertions.assertEquals(credentialPassword, credential.getPassword());

        homePage.logout();
    }

    @Test
    public void editCredential(){
        login("userWithCredential", "userWithCredential");

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);

        homePage.switchToCredentials();

        String credentialUrl = "updated credential URL";
        String credentialUsername = "updated credential username";
        String credentialPassword = "updated credential password";

        credentialPage = new CredentialPage(driver);
        credentialPage.editCredential(credentialUrl, credentialUsername, credentialPassword);

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);

        homePage.switchToCredentials();

        Credential credential = credentialPage.getFirstCredential();

        Assertions.assertEquals(credentialUrl, credential.getUrl());
        Assertions.assertEquals(credentialUsername, credential.getUsername());
        Assertions.assertEquals(credentialPassword, credential.getPassword());

        homePage.logout();
    }


    @Test
    public void deleteCredential(){
        login("userWithCredential", "userWithCredential");

        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        homePage.switchToCredentials();
        credentialPage = new CredentialPage(driver);
        credentialPage.delete();
        driver.get("http://localhost:" + port + "/home");
        homePage = new HomePage(driver);
        homePage.switchToCredentials();

        Assertions.assertEquals(0, driver.findElements(By.className("credentialUrl")).size());

        homePage.logout();
    }



    @AfterAll
    public static void afterAll() {
        driver.quit();
    }
}
