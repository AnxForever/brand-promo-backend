package com.brandpromo.aspect;

import com.brandpromo.entity.OperationLog;
import com.brandpromo.entity.User;
import com.brandpromo.mapper.OperationLogMapper;
import com.brandpromo.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final UserMapper userMapper;

    @AfterReturning("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                     "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                     "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logOperation(JoinPoint joinPoint) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) return;

            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;

            HttpServletRequest request = attrs.getRequest();
            String method = request.getMethod();
            String uri = request.getRequestURI();

            OperationLog opLog = new OperationLog();
            opLog.setAction(method + " " + uri);
            opLog.setTargetType(joinPoint.getSignature().getDeclaringType().getSimpleName());
            opLog.setIpAddress(request.getRemoteAddr());

            String username = auth.getName();
            User user = userMapper.findByUsername(username);
            if (user != null) {
                opLog.setUserId(user.getId());
            }

            operationLogMapper.insert(opLog);
        } catch (Exception e) {
            log.warn("Failed to log operation: {}", e.getMessage());
        }
    }
}
