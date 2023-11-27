package com.jxiang.blog.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class QiniuUtils {

  final private Environment environment;

  public QiniuUtils(final Environment environment) {
    this.environment = environment;
  }

  public Map<String, Object> upload(final MultipartFile file, final String fileName) {
    final Configuration cfg = new Configuration(Region.regionNa0());
    final UploadManager uploadManager = new UploadManager(cfg);
    final String bucket = environment.getProperty("credentials.qiniu.bucket");

    try {
      final byte[] uploadBytes = file.getBytes();

      final Auth auth = Auth.create(
          environment.getProperty("credentials.qiniu.accessKey"),
          Objects.requireNonNull(
              environment.getProperty("credentials.qiniu.accessSecretKey")));

      final String upToken = auth.uploadToken(bucket);
      final Response response = uploadManager.put(uploadBytes, fileName, upToken);

      JSON.parseObject(response.bodyString(), DefaultPutRet.class);
      final Map<String, Object> result = new HashMap<>();
      result.put("success", true);
      result.put("urn", environment.getProperty("credentials.qiniu.url"));
      return result;
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
    final Map<String, Object> result = new HashMap<>();
    result.put("success", false);
    result.put("urn", environment.getProperty("credentials.qiniu.url"));
    return result;
  }

}