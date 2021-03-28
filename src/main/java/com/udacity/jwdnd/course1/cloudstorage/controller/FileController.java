package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
@RequestMapping("files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public ModelAndView addFile(
            @RequestParam("fileUpload") MultipartFile file,
            ModelMap model,
            Principal principal
    ) {
        FileForm fileForm = null;
        String fileUploadErrorMessage = null;

        if (file.getOriginalFilename().isEmpty()){
            return new ModelAndView("redirect:/home", model);
        }

        if (!fileService.isFilenameAvailable(file.getOriginalFilename())) {
            fileUploadErrorMessage = "File already exists.";
        } else {
            try {
                fileForm = new FileForm(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        Long.toString(file.getSize()),
                        file.getBytes(),
                        principal.getName()
                );
            } catch (IOException e) {
                e.printStackTrace();
                fileUploadErrorMessage = e.getMessage();

            }
            fileService.addFile(fileForm);
        }

        if (fileUploadErrorMessage != null) {
            model.addAttribute("fileUploadError", fileUploadErrorMessage);
        }

        return new ModelAndView("redirect:/home", model);
    }

    @RequestMapping(value="/download/{id}", method=RequestMethod.GET)
    @ResponseBody
    public void downloadFile(
            HttpServletResponse response,
            @PathVariable Integer id) {

        File file = fileService.getFileById(id);
        if (file != null){
            response.setContentType(file.getContenttype());
            response.addHeader("Content-Disposition", "attachment; filename="+file.getFilename());
            try
            {
                InputStream fileInputStream = new ByteArrayInputStream(file.getFiledata());
                IOUtils.copy(fileInputStream, response.getOutputStream());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Integer id) {
        fileService.deleteFile(id);
        return "redirect:/home";
    }
}