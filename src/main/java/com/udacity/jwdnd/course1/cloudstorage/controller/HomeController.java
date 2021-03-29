package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private FileService fileService;

    @GetMapping("/home")
    public String landing(
            @ModelAttribute("newNote")  NoteForm noteForm,
            @ModelAttribute("newCredential") CredentialForm credentialForm,
            @RequestParam(value = "fileUploadError", required = false) String fileUploadError,
            Model model,
            Principal principal
    ){
        model.addAttribute("notes", noteService.getUserNotes(principal.getName()));
        model.addAttribute("credentials", credentialService.getUserCredentials(principal.getName()));
        model.addAttribute("files", fileService.getUserFiles(principal.getName()));
        model.addAttribute("fileUploadError", fileUploadError);

        return "home";
    }

}
