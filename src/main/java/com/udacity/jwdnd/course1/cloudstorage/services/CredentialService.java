package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.controller.form.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EncryptionService encryptionService;

    public void addCredential(CredentialForm credentialForm){
        User user = userMapper.getUser(credentialForm.getOwnerUsername());
        if (user != null) {
            credentialMapper.insert(
                    new Credential(
                            credentialForm.getUrl(),
                            credentialForm.getUsername(),
                            EncryptionService.PASSWORD,
                            credentialForm.getPassword(),
                            user.getUserId())
            );
        }
    }

    public List<CredentialDto> getUserCredentials(String username){
        User user = userMapper.getUser(username);
        if (user != null) {
            return credentialMapper.getUserCredentials(user.getUserId()).stream().map(
                    credential -> new CredentialDto(
                            credential.getCredentialid(),
                            credential.getUrl(),
                            credential.getUsername(),
                            credential.getPassword(),
                            encryptionService.decryptValue(
                                    credential.getPassword(),
                                    credential.getKey()
                            )
                    )
            ).collect(Collectors.toList());
        }
        return emptyList();
    }

    public void updateCredential(CredentialForm credentialForm){
        User user = userMapper.getUser(credentialForm.getUsername());
        if (user != null) {
            credentialMapper.update(
                    new Credential(
                            credentialForm.getCredentialid(),
                            credentialForm.getUrl(),
                            credentialForm.getUsername(),
                            null,
                            credentialForm.getPassword(),
                            user.getUserId())
            );
        }
    }

    public void deleteCredential(Integer id){
        credentialMapper.deleteById(id);
    }

}
