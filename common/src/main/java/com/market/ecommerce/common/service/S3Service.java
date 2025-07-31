package com.market.ecommerce.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String BUCKET_NAME;

    public void putObject(String key, InputStream inputStream, long contentLength) {
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3Client.putObject(req, RequestBody.fromInputStream(inputStream, contentLength));
    }

    public void deleteObject(String key) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3Client.deleteObject(req);
    }

    public String getUrl(String key) {
        GetUrlRequest req = GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        String encodedUrl = s3Client.utilities().getUrl(req).toString();
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }

    public void deleteObjectsWithPrefix(String prefix) {
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

        List<ObjectIdentifier> toDelete = listRes.contents().stream()
                .map(s3Obj -> ObjectIdentifier.builder().key(s3Obj.key()).build())
                .collect(Collectors.toList());

        if (!toDelete.isEmpty()) {
            DeleteObjectsRequest deleteReq = DeleteObjectsRequest.builder()
                    .bucket(BUCKET_NAME)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();

            s3Client.deleteObjects(deleteReq);
        }
    }
}
