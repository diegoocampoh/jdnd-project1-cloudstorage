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

        try {
            noteForm.setUsername(principal.getName());
            if (noteForm.getNoteid() != null){
                noteService.updateNote(noteForm);
                model.addAttribute("successMessage", "Note updated successfully");
            } else {
                noteService.addNote(noteForm);
                model.addAttribute("successMessage", "Note created successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating or updating note.");
        }

        return "result";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Integer id, Model model){
        try {
            noteService.deleteNote(id);
            model.addAttribute("successMessage", "Note deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error deleting note.");
        }
        return "result";
    }
}
