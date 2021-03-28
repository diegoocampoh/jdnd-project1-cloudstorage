package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotePage {

    private WebDriver driver;

    @FindBy(id = "addNoteButton")
    private WebElement addNoteButton;

    @FindBy(className = "editNoteButton")
    private WebElement editNoteButton;

    @FindBy(className = "deleteNoteButton")
    private WebElement deleteNoteButton;

    @FindBy(id = "confirmDeleteButton")
    private WebElement confirmDeleteButton;

    @FindBy(id = "noteModalConfirmButton")
    private WebElement noteModalConfirmButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(className = "noteTitle")
    private WebElement firstNoteTitle;

    @FindBy(className = "noteDescription")
    private WebElement firstNoteDescription;

    public NotePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.driver = driver;

    }

    public void addNote(String title, String desc){
        addNoteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(addNoteButton));
        addNoteButton.click();
        noteTitleInput = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(noteTitleInput));
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(desc);
        noteModalConfirmButton.click();
    }

    public Note getFirstNote() {
        firstNoteTitle = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(firstNoteTitle));
        firstNoteDescription = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(firstNoteDescription));

        return new Note(
                firstNoteTitle.getText(),
                firstNoteDescription.getText(),
        null);
    }

    public void editNote(String title, String desc){
        editNoteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(editNoteButton));
        editNoteButton.click();

        noteTitleInput = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOf(noteTitleInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(title);

        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(desc);

        noteModalConfirmButton.click();
    }

    public void delete(){
        deleteNoteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(deleteNoteButton));
        deleteNoteButton.click();

        confirmDeleteButton = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.elementToBeClickable(confirmDeleteButton));
        confirmDeleteButton.click();
    }

}
