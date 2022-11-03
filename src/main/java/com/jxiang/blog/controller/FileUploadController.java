package com.jxiang.blog.controller;

import com.jxiang.blog.util.beans.QiniuUtils;
import com.jxiang.blog.util.statics.SysUserThreadLocal;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/upload")
public class FileUploadController {

  @Autowired
  QiniuUtils qiniuUtils;

  @PostMapping
  public Result uploadArticleImage(@RequestBody MultipartFile file) {

    if (file.isEmpty()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    SysUser sysUser = SysUserThreadLocal.get();
    Long userId = sysUser.getId();

    String originalFilename = file.getOriginalFilename();
    String fileNameToUpload = "users/" + userId + "/" + originalFilename;

    Map<String, Object> result = qiniuUtils.upload(file, fileNameToUpload);
    Boolean success = (Boolean) result.get("success");

    if (success) {
      return Result.success(result.get("urn") + "/" + fileNameToUpload);
    }
    return Result.failure(ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

}
