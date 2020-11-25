package com.tsit.ocr.service;


import com.tsit.ocr.utils.FileUtil;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import net.sourceforge.tess4j.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        instance.setDatapath(tessdata); //tessdata目录
        instance.setLanguage("chi_sim");//选择字库文件（只需要文件名，不需要后缀名）
        try {
            BufferedImage image = ImageIO.read(file);
            String result = instance.doOCR(image);
            FileUtil.delteTempFile(file);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String,Object>> distinguishWithGrid(MultipartFile picture) throws Exception {
        File imageFile = new File(appFilePath+"media/temp/",picture.getOriginalFilename());
        imageFile = FileUtil.multipartFileToFile(picture,imageFile);
        ITesseract instance = new Tesseract();
        instance.setDatapath(tessdata); //tessdata目录
        instance.setLanguage("chi_sim");//选择字库文件（只需要文件名，不需要后缀名）
        BufferedImage bi = ImageIO.read(imageFile);
        int level = ITessAPI.TessPageIteratorLevel.RIL_SYMBOL;
        List<Word> words = instance.getWords(bi, level);
        List<Map<String,Object>> list = new ArrayList<>();
        for(Word w : words){
            Map<String,Object> data = new HashMap<>();
            data.put("word",w.getText());
            data.put("width",w.getBoundingBox().width);
            data.put("height",w.getBoundingBox().height);
            data.put("x",w.getBoundingBox().x);
            data.put("y",w.getBoundingBox().y);
            list.add(data);
        }
        return  list;
    }
}
