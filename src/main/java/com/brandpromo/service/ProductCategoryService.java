package com.brandpromo.service;

import com.brandpromo.entity.ProductCategory;
import com.brandpromo.mapper.ProductCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryMapper categoryMapper;

    /** 获取全部分类（扁平列表） */
    public List<ProductCategory> findAll() {
        return categoryMapper.findAll();
    }

    /** 获取分类树（parentId=0 为根节点） */
    public List<Map<String, Object>> getTree() {
        List<ProductCategory> all = categoryMapper.findAll();
        return buildTree(all, 0L);
    }

    private List<Map<String, Object>> buildTree(List<ProductCategory> all, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        for (ProductCategory c : all) {
            long pid = c.getParentId() != null ? c.getParentId() : 0L;
            if (pid == parentId) {
                Map<String, Object> node = new LinkedHashMap<>();
                node.put("id", c.getId());
                node.put("name", c.getName());
                node.put("parentId", c.getParentId());
                node.put("sortOrder", c.getSortOrder());
                node.put("icon", c.getIcon());
                node.put("status", c.getStatus());
                List<Map<String, Object>> children = buildTree(all, c.getId());
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                tree.add(node);
            }
        }
        return tree;
    }

    public ProductCategory findById(Long id) {
        return categoryMapper.findById(id);
    }

    public ProductCategory create(ProductCategory category) {
        if (category.getStatus() == null) category.setStatus(1);
        if (category.getParentId() == null) category.setParentId(0L);
        if (category.getSortOrder() == null) category.setSortOrder(0);
        categoryMapper.insert(category);
        return category;
    }

    public ProductCategory update(Long id, ProductCategory category) {
        category.setId(id);
        categoryMapper.update(category);
        return categoryMapper.findById(id);
    }

    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }
}
