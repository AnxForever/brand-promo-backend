package com.brandpromo.service;

import com.brandpromo.entity.Product;
import com.brandpromo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public Map<String, Object> findAll(String keyword, String category, Long merchantId, String sort, int page, int size) {
        int offset = (page - 1) * size;
        List<Product> list = productMapper.findAll(keyword, category, merchantId, sort, offset, size);
        int total = productMapper.countAll(keyword, category, merchantId);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public Product findById(Long id) {
        productMapper.incrementViewCount(id);
        return productMapper.findById(id);
    }

    public Product create(Product product) {
        product.setStatus(1);
        productMapper.insert(product);
        return product;
    }

    public Product update(Long id, Product product) {
        product.setId(id);
        productMapper.update(product);
        return productMapper.findById(id);
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    public List<String> getCategories() {
        return productMapper.findAllCategories();
    }
}
