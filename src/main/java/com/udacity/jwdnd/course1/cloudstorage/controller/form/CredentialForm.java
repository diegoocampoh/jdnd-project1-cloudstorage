package com.udacity.jwdnd.course1.cloudstorage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialForm {
    private Integer credentialid;
    private String url;
    private String ownerUsername;
    private String username;
    private String password;
}
