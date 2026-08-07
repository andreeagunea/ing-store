package com.ing.andreea.teastore.repository;

import com.ing.andreea.teastore.model.entity.TeaEntity;
import com.ing.andreea.teastore.model.enums.TeaCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeaRepository extends JpaRepository<TeaEntity, Long> {

    Optional<TeaEntity> findByNameIgnoreCase(String name);

    List<TeaEntity> findByCategory(TeaCategory category);

    List<TeaEntity> findAll();

    List<TeaEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<TeaEntity> findByStockQuantityGreaterThan(int quantity);

    boolean existsByNameIgnoreCase(String name);
}
