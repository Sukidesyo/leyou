package com.leyou.web;

import com.leyou.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @RequestMapping("/image")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file)
    {
        String url = uploadService.uploadFile(file);
        System.out.println("url=="+url);
        return ResponseEntity.ok().body(url);
    }
}
