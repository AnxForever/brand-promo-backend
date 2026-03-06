package com.brandpromo.service;

import com.brandpromo.entity.User;
import com.brandpromo.mapper.BrowseHistoryMapper;
import com.brandpromo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrowseHistoryService {

    private final BrowseHistoryMapper browseHistoryMapper;
    private final UserMapper userMapper;

    public void record(Long productId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return; // skip for anonymous users
        }
        User user = userMapper.findByUsername(auth.getName());
        if (user != null) {
            browseHistoryMapper.upsert(user.getId(), productId);
        }
    }
}
