package com.yuvaa.product_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
        Long id,
        String productName,
        String createdBy,
        LocalDateTime createdOn,
        String modifiedBy,
        LocalDateTime modifiedOn,
        List<ItemResponse> items
) {}