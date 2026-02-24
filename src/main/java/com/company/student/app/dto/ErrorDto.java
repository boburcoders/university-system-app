package com.company.student.app.dto;

import lombok.Setter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
public class ErrorDto {

    private String path;
    private String message;
    private int code;
    private LocalDateTime timestamp;

    public ErrorDto(String path, String message, int code) {
        this.path = path;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Tashkent")));
    }
}
