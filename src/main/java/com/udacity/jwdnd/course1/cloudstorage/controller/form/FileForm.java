package com.udacity.jwdnd.course1.cloudstorage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileForm {
    private String filename;
    private String contenttype;
    private String filesize;
    private byte[] data;
    private String username;
}

