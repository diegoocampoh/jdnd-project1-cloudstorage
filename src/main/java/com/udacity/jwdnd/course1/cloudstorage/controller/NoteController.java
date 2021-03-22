package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public String addNote(
            @ModelAttribute("newNote") NoteForm noteForm,
            Model model,
            Principal principal
    ){
        noteForm.setUsername(principal.getName());

        if (noteForm.getNoteid() != null){
            noteService.updateNote(noteForm);
        } else {
            noteService.addNote(noteForm);
        }

        model.addAttribute("notes", noteService.getUserNotes(principal.getName()));
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Integer id){
        noteService.deleteNote(id);
        return "redirect:/home";
    }
}
