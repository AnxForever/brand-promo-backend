package com.brandpromo.service;

import com.brandpromo.entity.Brand;
import com.brandpromo.mapper.BrandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandMapper brandMapper;

    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    public Brand findById(Long id) {
        return brandMapper.findById(id);
    }

    public Brand create(Brand brand) {
        brandMapper.insert(brand);
        return brand;
    }

    public Brand update(Long id, Brand brand) {
        brand.setId(id);
        brandMapper.update(brand);
        return brandMapper.findById(id);
    }

    public void delete(Long id) {
        brandMapper.deleteById(id);
    }
}
