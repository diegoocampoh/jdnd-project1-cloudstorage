package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static java.util.Collections.emptyList;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    public void addFile(FileForm fileForm){
        User user = userMapper.getUser(fileForm.getUsername());
        //TODO check if the filename is available
        if (user != null) {
            fileMapper.insert(new File(
                    fileForm.getFilename(),
                    fileForm.getContenttype(),
                    fileForm.getFilesize(),
                    user.getUserId(),
                    fileForm.getData()
            ));
        }
    }

    public boolean isFilenameAvailable(String filename){
        return fileMapper.getFile(filename) == null;
    }

    public File getFileById(Integer id){
        return fileMapper.getFileById(id);
    }

    public List<File> getUserFiles(String username){
        User user = userMapper.getUser(username);
        if (user != null) {
            return fileMapper.getUserFiles(user.getUserId());
        }
        return emptyList();
    }

    public void deleteFile(Integer id){
        fileMapper.deleteById(id);
    }

}
