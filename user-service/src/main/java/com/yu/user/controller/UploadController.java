package com.yu.user.controller;


import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Api("上传接口")
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping
    @ApiOperation("上传接口")
    public AjaxResult upload(MultipartFile  file) {
        log.info("上传文件：{}", file);
        String fileName = UUID.randomUUID().toString();
        String objectName = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY/MM/DD")) + "/";
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(objectName)
                .setSaveFilename(fileName)
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .upload();  //将文件上传到对应地方
        if(fileInfo == null){
            throw new RuntimeException("上传失败");
        }
        log.info("上传成功：{}", fileInfo);
        return AjaxResult.success(fileInfo);
    }
}
