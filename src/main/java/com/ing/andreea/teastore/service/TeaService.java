package com.ing.andreea.teastore.service;

import com.ing.andreea.teastore.dto.Tea;
import com.ing.andreea.teastore.dto.TeaRequest;
import com.ing.andreea.teastore.exception.DuplicateTeaException;
import com.ing.andreea.teastore.model.entity.TeaEntity;
import com.ing.andreea.teastore.model.enums.TeaCategory;
import com.ing.andreea.teastore.repository.TeaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TeaService {

    private static final Logger logger = LoggerFactory.getLogger(TeaService.class);

    private final TeaRepository teaRepository;

    public TeaService(TeaRepository teaRepository) {
        this.teaRepository = teaRepository;
    }

    public List<Tea> getAllTeas() {
        logger.info("Retrieve all teas");
        return teaRepository.findAll().stream().map(Tea::fromEntity).toList();
    }

    public List<Tea> getTeasByName(String name) {
        logger.info("Retrieve teas by name");
        return teaRepository.findByNameIgnoreCase(name).stream().map(Tea::fromEntity).toList();
    }

    public List<Tea> getTeasByCategory(TeaCategory category) {
        logger.info("Retrieve teas by category");
        return teaRepository.findByCategory(category).stream().map(Tea::fromEntity).toList();
    }

    public List<Tea> getTeasByPriceBetween(BigDecimal min, BigDecimal max) {
        logger.info("Retrieve teas by price between {} and {}", min, max);
        return teaRepository.findByPriceBetween(min, max).stream().map(Tea::fromEntity).toList();
    }

    public List<Tea> getTeasInStock(int quantity) {
        logger.info("Retrieve teas in stock with a quantity bigger than {}", quantity);
        return teaRepository.findByStockQuantityGreaterThan(quantity).stream().map(Tea::fromEntity).toList();
    }

    public Tea addTea(TeaRequest request) {
        logger.info("Adding new tea: {}", request.getName());

        if (teaRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateTeaException("A tea with the name '" + request.getName() + "' already exists");
        }

        TeaEntity tea = TeaEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .origin(request.getOrigin())
                .build();

        TeaEntity savedTea = teaRepository.save(tea);
        logger.info("Tea saved successfully with id: {}", savedTea.getId());

        return Tea.fromEntity(savedTea);
    }
}
