package com.jxiang.blog.controllers;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.utils.QiniuUtils;
import com.jxiang.blog.utils.SysUserThreadLocal;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("api/upload")
public class FileUploadController {

    @Autowired
    QiniuUtils qiniuUtils;

    @PostMapping
    public Result uploadArticleImage(@RequestBody MultipartFile file) {
        SysUser sysUser = SysUserThreadLocal.get();
        Long userId = sysUser.getId();

        String originalFilename = file.getOriginalFilename();
        String fileNameToUpload = "users/" + userId + "/" + originalFilename;

        Map<String, Object> result = qiniuUtils.upload(file, fileNameToUpload);
        Boolean success = (Boolean) result.get("success");

        if (success) {
            return Result.success(result.get("urn") + "/" + fileNameToUpload);
        }
        return Result.failure(ErrorCode.FILE_UPLOAD_FAILURE.getCode(), ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
    }

}