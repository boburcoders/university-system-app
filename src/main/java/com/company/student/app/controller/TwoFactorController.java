package com.company.student.app.controller;

import com.company.student.app.config.security.UserSession;
import com.company.student.app.dto.response.HttpApiResponse;
import com.company.student.app.dto.twoFA.TwoFactorConfirmRequest;
import com.company.student.app.dto.twoFA.TwoFactorSetupResponse;
import com.company.student.app.model.AuthUser;
import com.company.student.app.service.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/2fa")
@PreAuthorize(value = "isFullyAuthenticated()")
public class TwoFactorController {
    private final TwoFactorService twoFactorService;
    private final UserSession userSession;

    @PostMapping("/setup")
    public TwoFactorSetupResponse setup() {
        AuthUser user = userSession.getCurrentUser();
        return twoFactorService.setup(user);
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody TwoFactorConfirmRequest request) {
        AuthUser user = userSession.getCurrentUser();
        twoFactorService.confirm(user, request);
    }

    @PostMapping("/disable")
    public void disable(@RequestBody TwoFactorConfirmRequest request) {
        AuthUser user = userSession.getCurrentUser();
        twoFactorService.disable(user, request.code());
    }



    @GetMapping("/status")
    public ResponseEntity<HttpApiResponse<Boolean>> status() {
        AuthUser user = userSession.getCurrentUser();

        return ResponseEntity.ok(
                HttpApiResponse.<Boolean>builder()
                        .success(true)
                        .status(200)
                        .message("ok")
                        .data(Boolean.TRUE.equals(user.getTwoFactorEnabled()))
                        .build()
        );
    }
}
