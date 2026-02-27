package com.company.student.app.controller;

import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpApiResponse<String>> uploadFile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String fileName) {
        fileName = fileName.replace("..", "");
        Resource resource = fileService.getFileAsResource(fileName);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = java.net.URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
