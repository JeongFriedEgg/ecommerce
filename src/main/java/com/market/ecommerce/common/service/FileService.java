package com.market.ecommerce.common.service;

import com.market.ecommerce.common.exception.file.FileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.market.ecommerce.common.exception.file.FileErrorCode.FILE_UPLOAD_FAILED;
import static com.market.ecommerce.common.exception.file.FileErrorCode.FILE_URL_CONVERSION_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Service s3Service;
    private static final String UPLOAD_DIR = "images/";

    public String uploadImage(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String customFileName = String.format("%s/%s", UUID.randomUUID(), originalFileName);

        long contentLength = multipartFile.getSize();

        try {
            InputStream inputStream = multipartFile.getInputStream();
            s3Service.putObject(UPLOAD_DIR + customFileName, inputStream, contentLength);
        } catch (IOException e) {
            throw new FileException(FILE_UPLOAD_FAILED);
        }
        return s3Service.getUrl(UPLOAD_DIR + customFileName);
    }

    public List<String> uploadImages(List<MultipartFile> multipartFiles) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String url = uploadImage(file);
            urls.add(url);
        }
        return urls;
    }

    public void deleteImage(String imageUrl) {
        String key = extractKeyFromUrl(imageUrl);
        s3Service.deleteObject(key);
    }

    public void deleteImages(List<String> imageUrls) {
        for (String url : imageUrls) {
            deleteImage(url);
        }
    }

    private String extractKeyFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            String fullPath = url.getPath();
            String decoded = URLDecoder.decode(fullPath, StandardCharsets.UTF_8);
            return decoded.startsWith("/") ? decoded.substring(1) : decoded;
        } catch (MalformedURLException e) {
            throw new FileException(FILE_URL_CONVERSION_FAILED);
        }
    }
}
