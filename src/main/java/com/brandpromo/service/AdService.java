package com.brandpromo.service;

import com.brandpromo.entity.Advertisement;
import com.brandpromo.mapper.AdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdMapper adMapper;

    public Map<String, Object> findAll(Long merchantId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        List<Advertisement> list = adMapper.findAll(merchantId, status, offset, size);
        int total = adMapper.countAll(merchantId, status);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Advertisement findById(Long id) {
        return adMapper.findById(id);
    }

    public List<Advertisement> findActive() {
        return adMapper.findActive();
    }

    public Advertisement create(Advertisement ad) {
        ad.setStatus(0);  // pending review by default
        adMapper.insert(ad);
        return ad;
    }

    public Advertisement update(Long id, Advertisement ad) {
        ad.setId(id);
        adMapper.update(ad);
        return adMapper.findById(id);
    }

    public void updateStatus(Long id, Integer status) {
        adMapper.updateStatus(id, status);
    }

    public void delete(Long id) {
        adMapper.deleteById(id);
    }
}
