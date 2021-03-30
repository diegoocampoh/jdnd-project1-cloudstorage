package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.controller.form.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
@RequestMapping("files")
public class FileController implements HandlerExceptionResolver {

    @Autowired
    private FileService fileService;

    @PostMapping
    public ModelAndView addFile(
            @RequestParam("fileUpload") MultipartFile file,
            ModelMap model,
            Principal principal
    ) {
        FileForm fileForm = null;
        if (file.getOriginalFilename().isEmpty()){
            return new ModelAndView("redirect:/home", model);
        }

        if (!fileService.isFilenameAvailable(file.getOriginalFilename())) {
            model.addAttribute("errorMessage", "Error uploading file. File already exists.");
        } else {
            try {
                fileForm = new FileForm(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        Long.toString(file.getSize()),
                        file.getBytes(),
                        principal.getName()
                );
                fileService.addFile(fileForm);
                model.addAttribute("successMessage", "File created successfully");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error uploading file.");
            }
        }
        return new ModelAndView("result", model);
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
    public String deleteFile(@PathVariable Integer id, Model model) {
        try {
            fileService.deleteFile(id);
            model.addAttribute("successMessage", "File deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error deleting file.");
        }
        return "result";
    }

    @Override
    public ModelAndView resolveException(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object o,
            Exception e) {
        ModelAndView modelAndView = new ModelAndView("result");
        if (e instanceof MaxUploadSizeExceededException) {
            modelAndView.getModel().put("errorMessage", "File size exceeds limit, please upload a smaller file.");
        }
        return modelAndView;
    }
}