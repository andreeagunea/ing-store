package com.ing.andreea.teastore.dto;

import com.ing.andreea.teastore.model.entity.TeaEntity;
import com.ing.andreea.teastore.model.enums.TeaCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tea {
    private Long id;
    private String name;
    private String description;
    private TeaCategory category;
    private BigDecimal price;
    private int stockQuantity;
    private String origin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Tea fromEntity(TeaEntity teaEntity) {
        return Tea.builder()
                .id(teaEntity.getId())
                .name(teaEntity.getName())
                .description(teaEntity.getDescription())
                .category(teaEntity.getCategory())
                .price(teaEntity.getPrice())
                .stockQuantity(teaEntity.getStockQuantity())
                .origin(teaEntity.getOrigin())
                .createdAt(teaEntity.getCreatedAt())
                .updatedAt(teaEntity.getUpdatedAt())
                .build();
    }
}
