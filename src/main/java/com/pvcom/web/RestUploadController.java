package com.pvcom.web;

import com.pvcom.model.Response;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F:";

    @Autowired
    private Environment env;

    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("Single file upload!");
        String filestr = new Date().getTime() + "";
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfile), filestr);
        } catch (IOException e) {
            return new ResponseEntity<>(new Response("FAIL", 101, null), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new Response("SUCCESS", 100, filestr), new HttpHeaders(), HttpStatus.OK);

    }

    //save file
    private void saveUploadedFiles(List<MultipartFile> files, String name) throws IOException {
        if (!files.isEmpty()) {
            byte[] bytes = files.get(0).getBytes();
            Path path = Paths.get(env.getProperty("file.mount.path"), name + ".pdf");
            Files.write(path, bytes);
        }
    }

    @PostMapping("/api/download/{name}")
    public void downloadFile(@PathVariable("name") String name, HttpServletResponse response) {
        InputStream is = null;
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + new Date().getTime() + ".pdf");
            is = new FileInputStream(env.getProperty("file.mount.path") + File.separator + name + ".pdf");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
