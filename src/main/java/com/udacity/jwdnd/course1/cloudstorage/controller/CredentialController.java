package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.security.Principal;

@Controller
@RequestMapping("credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping
    public String addCredential(
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            Principal principal
    ){
        credentialForm.setOwnerUsername(principal.getName());
        credentialForm.setPassword(encryptionService.encryptValue(
                credentialForm.getPassword(),
                EncryptionService.PASSWORD
        ));

        if (credentialForm.getCredentialid() != null){
            credentialService.updateCredential(credentialForm);
        } else {
            credentialService.addCredential(credentialForm);
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Integer id){
        credentialService.deleteCredential(id);
        return "redirect:/home";
    }
}