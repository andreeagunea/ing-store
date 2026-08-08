package com.ing.andreea.teastore.controller;

import com.ing.andreea.teastore.dto.PriceUpdate;
import com.ing.andreea.teastore.dto.Tea;
import com.ing.andreea.teastore.dto.TeaRequest;
import com.ing.andreea.teastore.model.enums.TeaCategory;
import com.ing.andreea.teastore.service.TeaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/teas")
public class TeaController {

    private static final Logger logger = LoggerFactory.getLogger(TeaController.class);

    private final TeaService teaService;

    public TeaController(TeaService teaService) {
        this.teaService = teaService;
    }

    @GetMapping
    public ResponseEntity<List<Tea>> getAllTeas() {
        logger.info("Retrieving all teas");
        return ResponseEntity.ok(teaService.getAllTeas());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Tea>> getTeasByName(@RequestParam String name) {
        logger.info("Retrieving teas by name");
        return ResponseEntity.ok(teaService.getTeasByName(name));
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<Tea>> getTeasByCategory(@RequestParam TeaCategory category) {
        logger.info("Retrieving teas by category");
        return ResponseEntity.ok(teaService.getTeasByCategory(category));
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<Tea>> getTeasByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max
    ) {
        logger.info("Retrieving teas by price range");
        return ResponseEntity.ok(teaService.getTeasByPriceBetween(min, max));
    }

    @PostMapping
    public ResponseEntity<Tea> addTea(@RequestBody TeaRequest tea) {
        logger.info("Adding tea");
        return ResponseEntity.status(HttpStatus.CREATED).body(teaService.addTea(tea));
    }

    @PutMapping("/price/{id}")
    public ResponseEntity<Tea> updatePrice(
            @PathVariable Long id,
            @RequestBody PriceUpdate request) {
        logger.info("Update price for tea with id {}", id);
        return ResponseEntity.ok(teaService.updatePrice(id, request));
    }
}