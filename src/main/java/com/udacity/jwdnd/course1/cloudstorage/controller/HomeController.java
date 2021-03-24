package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/home")
    public String landing(
            @ModelAttribute("newNote")  NoteForm noteForm,
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            Model model,
            Principal principal
    ){
        model.addAttribute("notes", noteService.getUserNotes(principal.getName()));
        model.addAttribute("credentials", credentialService.getUserCredentials(principal.getName()));
        return "home";
    }

}
