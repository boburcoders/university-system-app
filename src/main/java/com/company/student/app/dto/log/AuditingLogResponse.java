package com.company.student.app.dto.log;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditingLogResponse {
    private String userName;
    private String ip;
    private String userAgent;
    private String deviceKey;
    private String action;
    private LocalDateTime createdAt;;
    private Long createdBy;
}
