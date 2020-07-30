package com.leyou.upload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {

    public String uploadFile(MultipartFile file)  {
        //保存路径
        File dest=new File("E:/leyouImages/images/"+file.getOriginalFilename());
        try {
            file.transferTo(dest);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回路径
        System.out.println("imageAdress=="+file.getOriginalFilename());
        return "http://image.leyou.com/images/"+file.getOriginalFilename();
    }
}
