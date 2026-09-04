package com.yuvaa.product_service.dto;

public record ItemResponse(
        Long id,
        Long productId,
        Integer quantity
) {}