package com.company.student.app.dto;

import com.company.student.app.model.enums.ErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    private ErrorCode errorCode;
    private int status;
    private String path;
    private LocalDateTime timestamp;
}
