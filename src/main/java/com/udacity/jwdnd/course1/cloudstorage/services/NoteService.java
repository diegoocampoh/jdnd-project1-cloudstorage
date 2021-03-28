package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;

    public void addNote(NoteForm noteForm){
        User user = userMapper.getUser(noteForm.getUsername());
        if (user != null) {
            noteMapper.insert(new Note(noteForm.getTitle(), noteForm.getContent(), user.getUserId()));
        }
    }

    public List<Note> getUserNotes(String username){
        User user = userMapper.getUser(username);
        if (user != null) {
            return noteMapper.getUserNotes(user.getUserId());
        }
        return emptyList();
    }

    public void updateNote(NoteForm noteForm){
        User user = userMapper.getUser(noteForm.getUsername());
        if (user != null) {
            noteMapper.update(
                    new Note(noteForm.getNoteid(), noteForm.getTitle(), noteForm.getContent(), user.getUserId())
            );
        }
    }

    public void deleteNote(Integer id){
        noteMapper.deleteById(id);
    }


}
