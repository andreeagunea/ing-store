package com.ing.andreea.teastore.dto;

import com.ing.andreea.teastore.model.enums.TeaCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeaRequest {

    @NotBlank(message = "Tea name is required")
    private String name;

    private String description;

    @NotNull(message = "Category is required")
    private TeaCategory category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stockQuantity;

    private String origin;
}
