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
        normalizeProduct(product);
        product.setStatus(product.getStatus() == null ? 1 : product.getStatus());
        productMapper.insert(product);
        return productMapper.findById(product.getId());
    }

    public Product update(Long id, Product product) {
        product.setId(id);
        normalizeProduct(product);
        productMapper.update(product);
        return productMapper.findById(id);
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
    }

    public List<String> getCategories() {
        return productMapper.findAllCategories();
    }

    public List<java.util.Map<String, Object>> diagStockColumn() {
        return productMapper.diagStockColumn();
    }

    public java.util.Map<String, Object> diagInsertStock(int stock) {
        return productMapper.diagInsertStock(stock);
    }

    private void normalizeProduct(Product product) {
        if (product == null) {
            return;
        }
        if (product.getPrice() != null && product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        }
        if (product.getPromoStatus() == null || product.getPromoStatus() != 1) {
            product.setPromoStatus(product.getPromoStatus() == null ? 0 : product.getPromoStatus());
            product.setPromoPrice(null);
            product.setPromoStartTime(null);
            product.setPromoEndTime(null);
        }
    }
}
