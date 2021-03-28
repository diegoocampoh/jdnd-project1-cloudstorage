package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialPage {

    private WebDriver driver;

    @FindBy(id = "addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id = "credentialModalConfirmButton")
    private WebElement credentialModalConfirmButton;

    @FindBy(id = "closeCredentialModal")
    private WebElement closeCredentialModal;

    @FindBy(className = "editCredentialButton")
    private WebElement editNoteButton;

    @FindBy(className = "deleteCredentialButton")
    private WebElement deleteNoteButton;

    @FindBy(id = "confirmDeleteButton")
    private WebElement confirmDeleteButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(className = "editCredentialButton")
    private WebElement existingCredentialEditButton;


    public CredentialPage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void addCredential(
            String url,
            String username,
            String password
    ){
        addCredentialButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(addCredentialButton));
        addCredentialButton.click();

        credentialUrlInput = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(credentialUrlInput));
        credentialUrlInput.clear();
        credentialUsernameInput.clear();
        credentialPasswordInput.clear();

        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.sendKeys(password);


        credentialModalConfirmButton.click();
    }

    public Credential getFirstCredential() {
        existingCredentialEditButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(existingCredentialEditButton));
        existingCredentialEditButton.click();

        credentialUrlInput = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(credentialUrlInput));

        Credential credential = new Credential(
                credentialUrlInput.getAttribute("value"),
                credentialUsernameInput.getAttribute("value"),
                null,
                credentialPasswordInput.getAttribute("value"),
                null
        );
        closeCredentialModal.click();
        return credential;
    }
//
//    public void editNote(String title, String desc){
//        editNoteButton = new WebDriverWait(driver, 15)
//                .until(ExpectedConditions.elementToBeClickable(editNoteButton));
//        editNoteButton.click();
//
//        noteTitleInput = new WebDriverWait(driver, 15)
//                .until(ExpectedConditions.visibilityOf(noteTitleInput));
//        noteTitleInput.clear();
//        noteTitleInput.sendKeys(title);
//
//        noteDescriptionInput.clear();
//        noteDescriptionInput.sendKeys(desc);
//
//        noteModalConfirmButton.click();
//    }

    public void delete(){
        deleteNoteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(deleteNoteButton));
        deleteNoteButton.click();

        confirmDeleteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(confirmDeleteButton));
        confirmDeleteButton.click();
    }

}
