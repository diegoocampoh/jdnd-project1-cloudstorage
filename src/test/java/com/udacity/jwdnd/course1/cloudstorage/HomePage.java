package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private WebDriver driver;

    @FindBy(id = "nav-files-tab")
    private WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentialsTab;

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void switchToNotes(){
        navNotesTab = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(navNotesTab));
        navNotesTab.click();
    }

    public void switchToCredentials(){
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(navCredentialsTab)).click();
    }

    public void logout(){
        WebDriverWait wait = new WebDriverWait(driver, 60);
        logoutButton = wait.until(webDriver -> webDriver.findElement(By.id("logoutButton")));
        logoutButton.click();
    }
}
