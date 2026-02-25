package com.company.student.app.config.storage;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // upload file
    public String uploadFile(MultipartFile file) throws Exception {

        if (file.isEmpty()) throw new RuntimeException("File empty");

        String original = file.getOriginalFilename();
        if (original == null) original = "file";

        original = original.replaceAll("[^a-zA-Z0-9.\\-]", "_");

        String fileName = UUID.randomUUID() + "-" + original;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return fileName;
    }

    // get file url
    public String getFileUrl(String fileName) {
        try {
            fileName = fileName.replace("..", "");
            minioClient.statObject(
                    io.minio.StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .method(io.minio.http.Method.GET)
                            .expiry(60 * 10) // 10 minutes
                            .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL: {}", fileName, e);
            throw new RuntimeException("File not found or cannot generate URL");
        }
    }
    public Resource getFileAsResource(String fileName) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );

            return new InputStreamResource(stream);

        } catch (Exception e) {
            log.error("MinIO error while getting file: {}", fileName, e);
            throw new RuntimeException("File not found or cannot be downloaded");
        }
    }
}
