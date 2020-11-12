package com.tsit.ocr.controller;


import com.tsit.ocr.service.OcrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
@CrossOrigin(allowCredentials ="true")
@Api(description = "图片转码接口")
public class OcrController {

    @Autowired
    private OcrService ocrService;



    @ApiOperation(value = "上传图片后解析图片返回字符串", httpMethod = "POST")
    @PostMapping(value = "distinguish")
    public String distinguish(@RequestParam("picture") MultipartFile picture){
        return ocrService.distinguish(picture);
    }


}
