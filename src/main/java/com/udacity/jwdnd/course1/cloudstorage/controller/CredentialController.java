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
            Model model,
            Principal principal
    ){
        try {
            credentialForm.setOwnerUsername(principal.getName());
            if (credentialForm.getCredentialid() != null){
                credentialService.updateCredential(credentialForm);
                model.addAttribute("successMessage", "Credential updated successfully");
            } else {
                credentialService.addCredential(credentialForm);
                model.addAttribute("successMessage", "Credential created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating credential.");
        }

        return "result";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Integer id, Model model){
        try {
            credentialService.deleteCredential(id);
            model.addAttribute("successMessage", "Credential deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error deleting credential.");
        }
        return "result";
    }
}