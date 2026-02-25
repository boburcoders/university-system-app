package com.company.student.app.service.impl;

import com.company.student.app.config.storage.MinioService;
import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final MinioService minioService;


    @Override
    public HttpApiResponse<String> uploadFile(MultipartFile file) {
        try {
            String fileName = minioService.uploadFile(file);

            return HttpApiResponse.<String>builder()
                    .success(true)
                    .status(200)
                    .message("ok")
                    .data(fileName)
                    .build();

        } catch (Exception e) {
            return HttpApiResponse.<String>builder()
                    .success(false)
                    .status(500)
                    .message("unable to upload image: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Resource getFileAsResource(String fileName) {
        return minioService.getFileAsResource(fileName);
    }
}
