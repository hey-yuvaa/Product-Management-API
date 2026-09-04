package com.yuvaa.product_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ProductRequest(
        @NotBlank(message = "Product name is mandatory")
        String productName,

        @Valid
        List<ItemRequest> items
) {}