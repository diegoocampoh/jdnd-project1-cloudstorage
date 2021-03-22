package com.udacity.jwdnd.course1.cloudstorage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteForm {
    private Integer noteid;
    private String username;
    private String title;
    private String content;
}
