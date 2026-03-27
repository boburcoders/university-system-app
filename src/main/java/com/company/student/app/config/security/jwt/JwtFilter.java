package com.company.student.app.config.security.jwt;

import com.company.student.app.config.security.CustomAuthenticationDetails;
import com.company.student.app.config.security.CustomUserDetailService;
import com.company.student.app.config.security.TenantContext;
import com.company.student.app.config.security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);

        try {
            if (!jwtService.isAccessToken(jwt)) {
                sendUnauthorized(response, "Access token required");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtService.extractUsername(jwt);
                Long universityId = jwtService.extractUniversityId(jwt);

                if (username == null || universityId == null) {
                    sendUnauthorized(response, "Invalid token payload");
                    return;
                }

                TenantContext.setTenantId(universityId);

                UserPrincipal userDetails =
                        (UserPrincipal) userDetailsService.loadUserByUsernameAndUniversity(username, universityId);

                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    sendUnauthorized(response, "Invalid or expired token");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                String ip = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                String deviceKey = generateDeviceKey(ip, userAgent, userDetails.getUserId());

                authToken.setDetails(
                        new CustomAuthenticationDetails(ip, userAgent, deviceKey)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response, "Invalid or expired token");
        } finally {
            TenantContext.clear();
        }
    }

    private String generateDeviceKey(String ip, String userAgent, Long userId) {
        String raw = userId + "|" + ip + "|" + userAgent;
        return Integer.toHexString(raw.hashCode());
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                {
                  "success": false,
                  "message": "%s"
                }
                """.formatted(message));
    }
}
