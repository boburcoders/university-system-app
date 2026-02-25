package com.company.student.app.service;

import com.company.student.app.dto.HttpApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    HttpApiResponse<String> uploadFile(MultipartFile file);

    Resource getFileAsResource(String fileName);
}
