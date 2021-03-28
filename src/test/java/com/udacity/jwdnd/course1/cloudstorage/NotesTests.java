package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
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
public class NotesTests {
    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    private static WebDriver driver;
    private static boolean userSignupDone = false;
    private static String existingUsername = "user";
    private static String existingPassword = "Tr$81m8TJ3iJ6";

    private LoginPage loginPage;
    private HomePage homePage;
    private NotePage notePage;

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

            userSignupDone = true;
        }
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


    @AfterAll
    public static void afterAll() {
        driver.quit();
    }
}
