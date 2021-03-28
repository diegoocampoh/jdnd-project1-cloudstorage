package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    public LoginPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void typeLoginDetails(
            String username,
            String passwword
    ){
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(passwword);
    }

    public void clickLogin(){
        submitButton.click();
    }

}
