package com.udacity.jwdnd.course1.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CredentialDto {
    private Integer credentialid;
    private String url;
    private String username;
    private String encryptedPassword;
    private String decryptedPassword;
}
