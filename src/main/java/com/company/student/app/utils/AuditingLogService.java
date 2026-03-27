package com.company.student.app.utils;

import com.company.student.app.dto.log.AuditingLogResponse;
import com.company.student.app.model.AuditingLog;
import com.company.student.app.model.AuthUser;
import com.company.student.app.repository.AuditingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditingLogService {
    private final AuditingLogRepository logRepository;

    public void log(Long orgId, AuthUser user, String ip, String userAgent, String deviceKey, String action) {
        AuditingLog log = new AuditingLog(orgId, user, ip, userAgent, deviceKey, action);
        logRepository.save(log);
    }

    public AuditingLogResponse mapToResponse(AuditingLog log) {
        return AuditingLogResponse.builder()
                .userName(log.getUser().getUsername())
                .ip(log.getIp())
                .action(log.getAction())
                .userAgent(log.getUserAgent())
                .deviceKey(log.getDeviceKey())
                .createdAt(log.getCreatedAt())
                .createdBy(log.getCreatedBy())
                .build();
    }
}
