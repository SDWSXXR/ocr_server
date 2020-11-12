package com.tsit.ocr.service;


import com.tsit.ocr.utils.FileUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class OcrService {

    @Value("${tessdata-path}")
    private String tessdata;
    @Value("${app-file-path}")
    private String appFilePath;

    public String distinguish(MultipartFile picture) {
        if (picture.equals("") || picture.getSize() <= 0) {
            return "图片为空！";
        }
        File file = new File(appFilePath+"media/temp/",picture.getOriginalFilename());
        file = FileUtil.multipartFileToFile(picture,file);
        ITesseract instance = new Tesseract();
        instance.setDatapath(tessdata); //相对目录，这个时候tessdata目录和src目录平级
        instance.setLanguage("chi_sim");//选择字库文件（只需要文件名，不需要后缀名）
        try {
            BufferedImage image = ImageIO.read(file);
            String result = instance.doOCR(image);
            FileUtil.delteTempFile(file);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
