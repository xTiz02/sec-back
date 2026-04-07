package com.prd.seccontrol.config.aws;

import com.prd.seccontrol.util.SEConstants;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

  @Autowired
  private Environment environment;

  public String getS3BucketName() {
    return SEConstants.S3_UPLOADS_BUCKET_NAME;
  }

  public String accessKey() {
    return SEConstants.S3_ACCESS_KEY_ID != null ? SEConstants.S3_ACCESS_KEY_ID
        : environment.getProperty("S3_ACCESS_KEY_ID");
  }

  public String secretKey() {
    return SEConstants.S3_SECRET_KEY_ACCESS != null ? SEConstants.S3_SECRET_KEY_ACCESS
        : environment.getProperty("S3_SECRET_KEY_ACCESS");
  }

  public String region() {
    return SEConstants.S3_REGION != null ? SEConstants.S3_REGION : environment.getProperty("S3_REGION");
  }

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .credentialsProvider(() -> AwsBasicCredentials.create(accessKey(), secretKey()))
        .endpointOverride(URI.create("https://s3." + region() + ".amazonaws.com"))
        .region(Region.of(region()))
        .build();
  }
}
