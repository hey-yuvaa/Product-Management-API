package com.yuvaa.product_service.service;

import com.yuvaa.product_service.dto.ItemResponse;
import com.yuvaa.product_service.dto.ProductRequest;
import com.yuvaa.product_service.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    ProductResponse getProductById(Long id);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<ItemResponse> getItemsByProductId(Long productId);
}