package com.prd.seccontrol.service.inter;

import com.prd.seccontrol.util.SEConstants;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class AwsS3Service {

  @Autowired
  private S3Client s3Client;

  public Boolean uploadFile(String key, MultipartFile file) throws IOException {
    byte[] fileContent = file.getBytes();
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(SEConstants.S3_UPLOADS_BUCKET_NAME)
        .key(key)
        .contentType(file.getContentType()) // Ajusta el tipo de contenido según el archivo
        .acl("public-read")
//        .metadata(Map.of("x-amz-meta-original-filename",
//            file.getOriginalFilename())) // Puedes agregar metadatos personalizados
        .build();

    PutObjectResponse putObjectResponse = this.s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(fileContent));

    return putObjectResponse.sdkHttpResponse().isSuccessful();
  }


  public String getFileUrl(String key) {
    return "https://" + SEConstants.S3_UPLOADS_BUCKET_NAME + ".s3." + SEConstants.S3_REGION
        + ".amazonaws.com/" + key;
  }

}
