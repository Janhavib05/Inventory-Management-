package com.stockpro.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ─── Request DTO (Create / Update) ───────────────────────────────────────────
public class ProductDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Name is required")
        @Size(max = 100)
        private String name;

        @NotBlank(message = "Description is required")
        @Size(max = 500)
        private String desc;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be >= 0")
        private BigDecimal price;

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be >= 0")
        private Integer qty;
    }

    // Partial update (all fields optional)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchRequest {

        @Size(max = 100)
        private String name;

        @Size(max = 500)
        private String desc;

        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal price;

        @Min(0)
        private Integer qty;
    }

    // ─── Response DTO ────────────────────────────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String desc;
        private BigDecimal price;
        private Integer qty;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    // ─── Summary DTO ─────────────────────────────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private long total;
        private long lowStock;
        private BigDecimal totalValue;
    }
}
