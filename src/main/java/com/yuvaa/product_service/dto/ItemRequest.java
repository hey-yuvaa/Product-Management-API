package com.yuvaa.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemRequest(
        @NotNull(message = "Item quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {}