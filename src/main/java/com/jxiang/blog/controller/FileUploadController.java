package com.jxiang.blog.controller;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.util.QiniuUtils;
import com.jxiang.blog.util.SysUserThreadLocal;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/upload")
public class FileUploadController {

  @Autowired
  private QiniuUtils qiniuUtils;

  @PostMapping
  public Result uploadArticleImage(@RequestBody final MultipartFile file) {

    if (file.isEmpty()) {
      return Result.failure(
          ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    final SysUser sysUser = SysUserThreadLocal.get();
    final Long userId = sysUser.getId();

    final String originalFilename = file.getOriginalFilename();
    final String fileNameToUpload = "users/" + userId + "/" + originalFilename;

    final Map<String, Object> result = qiniuUtils.upload(file, fileNameToUpload);
    final Boolean success = (Boolean) result.get("success");

    if (success) {
      return Result.success(result.get("urn") + "/" + fileNameToUpload);
    }
    return Result.failure(
        ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

}
